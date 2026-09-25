package com.hesabla.auth.security;

/**
 * JWT-dən çıxarılan, autentifikasiya olunmuş istifadəçinin minimal
 * məlumatı. Bu, Spring Security-nin authentication obyektinin
 * "principal"-ı kimi saxlanılır və @AuthenticationPrincipal ilə
 * birbaşa controller metodlarına inject oluna bilər — hər dəfə yenidən
 * JWT parse etməyə ehtiyac qalmır.
 */
public record AuthenticatedUser(Long userId, Long tenantId, String role) {
}
