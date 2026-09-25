package com.hesabla.invoicing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank(message = "Ad boş ola bilməz") String name,
        @Email(message = "Email formatı düzgün deyil") String email,
        String phone,
        String address,
        String taxId
) {
}
