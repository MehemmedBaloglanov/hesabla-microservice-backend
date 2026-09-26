package com.hesabla.auth.dto;

public record CurrentUserResponse(
        Long userId,
        Long tenantId,
        String tenantName,
        String email,
        String role
) {
}
