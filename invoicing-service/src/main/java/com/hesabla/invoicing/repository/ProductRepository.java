package com.hesabla.invoicing.repository;

import com.hesabla.invoicing.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByTenantId(Long tenantId, Pageable pageable);

    Optional<Product> findByIdAndTenantId(Long id, Long tenantId);
}