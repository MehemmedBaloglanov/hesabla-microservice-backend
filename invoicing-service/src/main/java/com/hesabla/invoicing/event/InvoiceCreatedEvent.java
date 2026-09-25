package com.hesabla.invoicing.event;

import java.math.BigDecimal;
import java.time.Instant;

public record InvoiceCreatedEvent(
        Long invoiceId,
        String invoiceNumber,
        Long tenantId,
        Long customerId,
        BigDecimal totalAmount,
        Instant occurredAt
) {
}
