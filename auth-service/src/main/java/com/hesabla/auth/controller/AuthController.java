package com.hesabla.auth.controller;

import com.hesabla.auth.dto.AuthResponse;
import com.hesabla.auth.dto.CurrentUserResponse;
import com.hesabla.auth.dto.InviteRequest;
import com.hesabla.auth.dto.InviteResponse;
import com.hesabla.auth.dto.LoginRequest;
import com.hesabla.auth.dto.RegisterRequest;
import com.hesabla.auth.dto.TeamMemberResponse;
import com.hesabla.auth.security.AuthenticatedUser;
import com.hesabla.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(@AuthenticationPrincipal AuthenticatedUser currentUser) {
        return ResponseEntity.ok(authService.getCurrentUser(currentUser.userId()));
    }

    @GetMapping("/team")
    public ResponseEntity<List<TeamMemberResponse>> team(@AuthenticationPrincipal AuthenticatedUser currentUser) {
        return ResponseEntity.ok(authService.listTeam(currentUser.tenantId()));
    }

    @PostMapping("/team/invite")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<InviteResponse> invite(@AuthenticationPrincipal AuthenticatedUser currentUser,
                                                 @Valid @RequestBody InviteRequest request) {
        InviteResponse response = authService.invite(currentUser.tenantId(), request);
        return ResponseEntity.ok(response);
    }
}