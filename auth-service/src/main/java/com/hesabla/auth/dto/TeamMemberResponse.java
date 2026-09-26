package com.hesabla.auth.dto;

import java.time.Instant;

public record TeamMemberResponse(
        Long userId,
        String email,
        String role,
        Instant createdAt
) {
}
