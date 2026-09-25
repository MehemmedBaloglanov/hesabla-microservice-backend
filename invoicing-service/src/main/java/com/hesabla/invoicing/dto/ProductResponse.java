package com.hesabla.invoicing.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal unitPrice,
        String unit,
        Instant createdAt
) {
}
