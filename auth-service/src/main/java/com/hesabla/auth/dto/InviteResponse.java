package com.hesabla.auth.dto;

public record InviteResponse(
        Long userId,
        String email,
        String role,
        String tenantName
) {
}
