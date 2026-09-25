package com.hesabla.invoicing.dto;

import java.math.BigDecimal;

public record InvoiceLineResponse(
        Long id,
        Long productId,
        String description,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
}
