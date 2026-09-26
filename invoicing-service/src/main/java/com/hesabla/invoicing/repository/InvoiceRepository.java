package com.hesabla.invoicing.repository;

import com.hesabla.invoicing.domain.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Page<Invoice> findByTenantId(Long tenantId, Pageable pageable);

    Optional<Invoice> findByIdAndTenantId(Long id, Long tenantId);

    @Query("SELECT i.status AS status, COUNT(i) AS count, COALESCE(SUM(i.totalAmount), 0) AS sum " +
            "FROM Invoice i WHERE i.tenantId = :tenantId GROUP BY i.status")
    List<InvoiceStatusAggregate> aggregateByStatus(@Param("tenantId") Long tenantId);
}