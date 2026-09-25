package com.hesabla.auth.dto;

public record AuthResponse(
        String token,
        Long userId,
        Long tenantId,
        String tenantName,
        String email,
        String role
) {
}
