package com.hesabla.invoicing.service;

import com.hesabla.invoicing.domain.Customer;
import com.hesabla.invoicing.domain.Invoice;
import com.hesabla.invoicing.domain.InvoiceLine;
import com.hesabla.invoicing.domain.InvoiceStatus;
import com.hesabla.invoicing.domain.Product;
import com.hesabla.invoicing.dto.*;
import com.hesabla.invoicing.exception.ResourceNotFoundException;
import com.hesabla.invoicing.repository.CustomerRepository;
import com.hesabla.invoicing.repository.InvoiceRepository;
import com.hesabla.invoicing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          CustomerRepository customerRepository,
                          ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public InvoiceResponse create(Long tenantId, Long userId, InvoiceRequest request) {
        Customer customer = customerRepository.findByIdAndTenantId(request.customerId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Müştəri tapılmadı"));

        Invoice invoice = new Invoice();
        invoice.setTenantId(tenantId);
        invoice.setInvoiceNumber(generateInvoiceNumber(tenantId));
        invoice.setCustomer(customer);
        invoice.setIssueDate(LocalDate.now());
        invoice.setDueDate(request.dueDate());
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setCreatedByUserId(userId);

        BigDecimal total = BigDecimal.ZERO;

        for (InvoiceLineRequest lineRequest : request.lines()) {
            InvoiceLine line = buildLine(tenantId, lineRequest);
            invoice.addLine(line);
            total = total.add(line.getLineTotal());
        }

        invoice.setTotalAmount(total);

        invoiceRepository.save(invoice);
        return toResponse(invoice);
    }

    private InvoiceLine buildLine(Long tenantId, InvoiceLineRequest request) {
        Product product = null;
        String description = request.description();
        BigDecimal unitPrice = request.unitPrice();

        if (request.productId() != null) {
            product = productRepository.findByIdAndTenantId(request.productId(), tenantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Məhsul tapılmadı"));
            if (description == null || description.isBlank()) {
                description = product.getName();
            }
            if (unitPrice == null) {
                unitPrice = product.getUnitPrice();
            }
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Sətir üçün ya productId, ya da description göstərilməlidir");
        }
        if (unitPrice == null) {
            throw new IllegalArgumentException("Sətir üçün ya productId, ya da unitPrice göstərilməlidir");
        }

        BigDecimal lineTotal = unitPrice.multiply(request.quantity())
                .setScale(2, RoundingMode.HALF_UP);

        InvoiceLine line = new InvoiceLine();
        line.setProduct(product);
        line.setDescription(description);
        line.setQuantity(request.quantity());
        line.setUnitPrice(unitPrice);
        line.setLineTotal(lineTotal);
        return line;
    }

    private String generateInvoiceNumber(Long tenantId) {
        long nextSequence = invoiceRepository.countByTenantId(tenantId) + 1;
        int year = LocalDate.now().getYear();
        return "INV-" + year + "-" + String.format("%05d", nextSequence);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> listAll(Long tenantId) {
        return invoiceRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getOne(Long tenantId, Long id) {
        Invoice invoice = invoiceRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Faktura tapılmadı"));
        return toResponse(invoice);
    }

    public InvoiceResponse updateStatus(Long tenantId, Long id, InvoiceStatusUpdateRequest request) {
        Invoice invoice = invoiceRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Faktura tapılmadı"));

        invoice.setStatus(request.status());
        return toResponse(invoice);
    }

    public void delete(Long tenantId, Long id) {
        Invoice invoice = invoiceRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Faktura tapılmadı"));
        invoiceRepository.delete(invoice);
    }

    private InvoiceResponse toResponse(Invoice invoice) {
        List<InvoiceLineResponse> lineResponses = invoice.getLines().stream()
                .map(l -> new InvoiceLineResponse(
                        l.getId(),
                        l.getProduct() != null ? l.getProduct().getId() : null,
                        l.getDescription(),
                        l.getQuantity(),
                        l.getUnitPrice(),
                        l.getLineTotal()
                ))
                .toList();

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getCustomer().getId(),
                invoice.getCustomer().getName(),
                invoice.getIssueDate(),
                invoice.getDueDate(),
                invoice.getStatus(),
                invoice.getTotalAmount(),
                lineResponses,
                invoice.getCreatedByUserId(),
                invoice.getCreatedAt()
        );
    }
}
