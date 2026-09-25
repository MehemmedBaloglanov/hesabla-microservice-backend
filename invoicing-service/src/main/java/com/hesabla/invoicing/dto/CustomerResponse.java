package com.hesabla.invoicing.dto;

import java.time.Instant;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String taxId,
        Instant createdAt
) {
}