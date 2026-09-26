package com.hesabla.invoicing.repository;

import com.hesabla.invoicing.domain.InvoiceSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceSequenceRepository extends JpaRepository<InvoiceSequence, Long> {

    /**
     * Tenant üçün növbəti ardıcıl faktura nömrəsini ATOMİK şəkildə qaytarır.
     * Qəsdən @Modifying işarələnməyib — RETURNING bəndi olan bu UPSERT
     * Postgres tərəfindən nəticə sətri kimi qaytarılır, @Modifying isə
     * Spring Data-nı executeUpdate() (nəticəsiz) çağırmağa məcbur edərdi.
     * Sətir kilidləməsi (Postgres-in daxili mexanizmi) paralel sorğuları
     * təhlükəsiz sıralayır — iki paralel çağırış heç vaxt eyni nömrəni almaz.
     */
    @Query(value = """
            INSERT INTO invoice_sequences (tenant_id, last_number)
            VALUES (:tenantId, 1)
            ON CONFLICT (tenant_id)
            DO UPDATE SET last_number = invoice_sequences.last_number + 1
            RETURNING last_number
            """, nativeQuery = true)
    long nextSequence(@Param("tenantId") Long tenantId);
}
