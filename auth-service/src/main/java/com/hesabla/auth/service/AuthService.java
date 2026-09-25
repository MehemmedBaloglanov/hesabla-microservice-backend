package com.hesabla.auth.service;

import com.hesabla.auth.domain.Role;
import com.hesabla.auth.domain.Tenant;
import com.hesabla.auth.domain.User;
import com.hesabla.auth.dto.AuthResponse;
import com.hesabla.auth.dto.LoginRequest;
import com.hesabla.auth.dto.RegisterRequest;
import com.hesabla.auth.repository.TenantRepository;
import com.hesabla.auth.repository.UserRepository;
import com.hesabla.auth.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(TenantRepository tenantRepository, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Bu email artıq istifadə olunur");
        }

        Tenant tenant = new Tenant();
        tenant.setName(request.tenantName());
        tenant = tenantRepository.save(tenant);

        User user = new User();
        user.setTenant(tenant);
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.OWNER);
        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), tenant.getId(),
                user.getRole().name(), tenant.getName(), user.getEmail());

        return new AuthResponse(token, user.getId(), tenant.getId(),
                tenant.getName(), user.getEmail(), user.getRole().name());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Email və ya şifrə yanlışdır"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Email və ya şifrə yanlışdır");
        }

        Tenant tenant = user.getTenant();
        String token = jwtService.generateToken(user.getId(), tenant.getId(),
                user.getRole().name(), tenant.getName(), user.getEmail());

        return new AuthResponse(token, user.getId(), tenant.getId(),
                tenant.getName(), user.getEmail(), user.getRole().name());
    }
}
