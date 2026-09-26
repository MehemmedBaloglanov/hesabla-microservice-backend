package com.hesabla.invoicing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Hər tenant üçün faktura nömrələmə sayğacı. Bu entity özü birbaşa
 * save()/find() ilə istifadə olunmur — yalnız InvoiceSequenceRepository-dəki
 * atomik native UPSERT sorğusu üçün JPA-nın tələb etdiyi "mapped type"-ı
 * təmin edir.
 */
@Entity
@Table(name = "invoice_sequences")
public class InvoiceSequence {

    @Id
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "last_number", nullable = false)
    private Long lastNumber;

    protected InvoiceSequence() {
    }
}