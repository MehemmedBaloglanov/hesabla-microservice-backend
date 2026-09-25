package com.hesabla.auth.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (jwtService.isValid(token)) {
            Claims claims = jwtService.parseClaims(token);

            Long userId = Long.valueOf(claims.getSubject());
            // DİQQƏT: claims.get("tenantId", Long.class) TƏHLÜKƏLİDİR.
            // JJWT+Jackson kiçik ədədləri (məs. 1) JSON-dan geri
            // parse edəndə Integer kimi saxlayır, Long kimi yox —
            // birbaşa Long.class istəsən ClassCastException alarsan.
            // Number.class istəyib sonra .longValue() çağırmaq hər iki
            // halda (Integer və ya Long) düzgün işləyir.
            Number tenantIdRaw = claims.get("tenantId", Number.class);
            Long tenantId = tenantIdRaw != null ? tenantIdRaw.longValue() : null;
            String role = claims.get("role", String.class);

            AuthenticatedUser principal = new AuthenticatedUser(userId, tenantId, role);

            var authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}