package com.hesabla.invoicing.repository;

import com.hesabla.invoicing.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByTenantId(Long tenantId);

    Optional<Invoice> findByIdAndTenantId(Long id, Long tenantId);
}
