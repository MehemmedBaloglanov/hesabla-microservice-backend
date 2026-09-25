package com.hesabla.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Şirkət adı boş ola bilməz")
        String tenantName,
        @NotBlank @Email(message = "Email formatı düzgün deyil")
        String email,
        @NotBlank @Size(min = 6, message = "Şifrə ən azı 6 simvol olmalıdır")
        String password
) {
}
