package com.hesabla.invoicing.event;

import java.time.Instant;

public record InvoiceStatusChangedEvent(
        Long invoiceId,
        String invoiceNumber,
        Long tenantId,
        String oldStatus,
        String newStatus,
        Instant occurredAt
) {
}
