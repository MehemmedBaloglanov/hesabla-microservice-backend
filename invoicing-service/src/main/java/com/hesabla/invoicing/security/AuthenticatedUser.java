package com.hesabla.invoicing.security;

public record AuthenticatedUser(Long userId,
                                Long tenantId,
                                String role) {
}
