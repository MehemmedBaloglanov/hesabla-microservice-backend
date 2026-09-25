package com.hesabla.invoicing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Ad boş ola bilməz") String name,
        String description,
        @NotNull(message = "Qiymət göstərilməlidir")
        @DecimalMin(value = "0.0", message = "Qiymət mənfi ola bilməz")
        BigDecimal unitPrice,
        String unit
) {
}