package com.hesabla.invoicing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InvoiceLineRequest(
        Long productId,
        String description,
        @NotNull(message = "Miqdar göstərilməlidir")
        @DecimalMin(value = "0.001", message = "Miqdar müsbət olmalıdır")
        BigDecimal quantity,
        @DecimalMin(value = "0.0", message = "Vahid qiymət mənfi ola bilməz")
        BigDecimal unitPrice
) {
}