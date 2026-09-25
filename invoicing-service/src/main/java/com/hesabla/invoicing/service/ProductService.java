package com.hesabla.invoicing.service;

import com.hesabla.invoicing.domain.Product;
import com.hesabla.invoicing.dto.ProductRequest;
import com.hesabla.invoicing.dto.ProductResponse;
import com.hesabla.invoicing.exception.ResourceNotFoundException;
import com.hesabla.invoicing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(Long tenantId, ProductRequest request) {
        Product product = new Product();
        product.setTenantId(tenantId);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setUnitPrice(request.unitPrice());
        if (request.unit() != null && !request.unit().isBlank()) {
            product.setUnit(request.unit());
        }

        productRepository.save(product);
        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listAll(Long tenantId) {
        return productRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getOne(Long tenantId, Long id) {
        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Məhsul tapılmadı"));
        return toResponse(product);
    }

    public ProductResponse update(Long tenantId, Long id, ProductRequest request) {
        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Məhsul tapılmadı"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setUnitPrice(request.unitPrice());
        if (request.unit() != null && !request.unit().isBlank()) {
            product.setUnit(request.unit());
        }

        return toResponse(product);
    }

    public void delete(Long tenantId, Long id) {
        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Məhsul tapılmadı"));
        productRepository.delete(product);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getUnit(),
                product.getCreatedAt()
        );
    }
}
