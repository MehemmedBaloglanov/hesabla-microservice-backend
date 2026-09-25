package com.hesabla.invoicing.repository;

import com.hesabla.invoicing.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByTenantId(Long tenantId);

    Optional<Customer> findByIdAndTenantId(Long id, Long tenantId);
}
