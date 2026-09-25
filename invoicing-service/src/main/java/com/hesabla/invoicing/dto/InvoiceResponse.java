package com.hesabla.invoicing.dto;

import com.hesabla.invoicing.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record InvoiceResponse(
        Long id,
        String invoiceNumber,
        Long customerId,
        String customerName,
        LocalDate issueDate,
        LocalDate dueDate,
        InvoiceStatus status,
        BigDecimal totalAmount,
        List<InvoiceLineResponse> lines,
        Long createdByUserId,
        Instant createdAt
) {
}
