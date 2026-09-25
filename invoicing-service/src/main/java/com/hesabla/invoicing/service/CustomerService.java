package com.hesabla.invoicing.service;

import com.hesabla.invoicing.domain.Customer;
import com.hesabla.invoicing.dto.CustomerRequest;
import com.hesabla.invoicing.dto.CustomerResponse;
import com.hesabla.invoicing.exception.ResourceNotFoundException;
import com.hesabla.invoicing.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse create(Long tenantId, CustomerRequest request) {
        Customer customer = new Customer();
        customer.setTenantId(tenantId);
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setAddress(request.address());
        customer.setTaxId(request.taxId());

        customerRepository.save(customer);
        return toResponse(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> listAll(Long tenantId) {
        return customerRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getOne(Long tenantId, Long id) {
        Customer customer = customerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Müştəri tapılmadı"));
        return toResponse(customer);
    }

    public CustomerResponse update(Long tenantId, Long id, CustomerRequest request) {
        Customer customer = customerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Müştəri tapılmadı"));

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setAddress(request.address());
        customer.setTaxId(request.taxId());

        // DİQQƏT: burada customerRepository.save(customer) ÇAĞIRMIRIQ!
        // Metod @Transactional olduğu üçün "customer" obyekti artıq
        // Hibernate-in idarəetdiyi (managed) vəziyyətdədir. Transaction
        // commit olanda Hibernate "dirty checking" ilə (obyektin
        // sahələrini yüklənən andakı ilə müqayisə edib) dəyişiklikləri
        // avtomatik SQL UPDATE-ə çevirir. save() yalnız YENİ (hələ DB-də
        // olmayan) entity-lər üçün lazımdır.
        return toResponse(customer);
    }

    public void delete(Long tenantId, Long id) {
        Customer customer = customerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Müştəri tapılmadı"));
        customerRepository.delete(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getTaxId(),
                customer.getCreatedAt()
        );
    }
}
