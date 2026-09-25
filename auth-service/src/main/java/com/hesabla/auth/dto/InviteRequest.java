package com.hesabla.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InviteRequest(
        @NotBlank(message = "Email boş ola bilməz")
        @Email(message = "Email formatı düzgün deyil")
        String email,

        @NotBlank(message = "Müvəqqəti parol boş ola bilməz")
        @Size(min = 6, message = "Müvəqqəti parol ən azı 6 simvol olmalıdır")
        String temporaryPassword,

        @NotBlank(message = "Rol göstərilməlidir")
        String role
) {
}