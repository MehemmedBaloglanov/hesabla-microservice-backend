package com.hesabla.invoicing.repository;

import com.hesabla.invoicing.domain.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Page<Invoice> findByTenantId(Long tenantId, Pageable pageable);

    Optional<Invoice> findByIdAndTenantId(Long id, Long tenantId);
}