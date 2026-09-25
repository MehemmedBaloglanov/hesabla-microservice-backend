package com.hesabla.auth.controller;

import com.hesabla.auth.dto.AuthResponse;
import com.hesabla.auth.dto.LoginRequest;
import com.hesabla.auth.dto.RegisterRequest;
import com.hesabla.auth.service.AuthService;
import jakarta.validation.Valid;
import com.hesabla.auth.dto.InviteRequest;
import com.hesabla.auth.dto.InviteResponse;
import com.hesabla.auth.security.AuthenticatedUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @PostMapping("/team/invite")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<InviteResponse> invite(@AuthenticationPrincipal AuthenticatedUser currentUser,
                                                 @Valid @RequestBody InviteRequest request) {
        InviteResponse response = authService.invite(currentUser.tenantId(), request);
        return ResponseEntity.ok(response);
    }
}
