package com.smartlibrary.service.impl;

import com.smartlibrary.dto.auth.AuthResponse;
import com.smartlibrary.dto.auth.LoginRequest;
import com.smartlibrary.dto.auth.RegisterRequest;
import com.smartlibrary.entity.Role;
import com.smartlibrary.entity.User;
import com.smartlibrary.exception.DuplicateResourceException;
import com.smartlibrary.repository.UserRepository;
import com.smartlibrary.security.JwtService;
import com.smartlibrary.security.TokenBlacklist;
import com.smartlibrary.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service.
 *
 * <p>Demonstrates constructor injection, {@code @Transactional} write methods,
 * BCrypt password hashing and JWT issuance/blacklisting.</p>
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlacklist tokenBlacklist;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           TokenBlacklist tokenBlacklist) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        Role role = resolveRole(request.getRole());
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(role)
                .build();

        User saved = userRepository.save(user);
        log.info("Registered new user id={} email={} role={}", saved.getId(), saved.getEmail(), role);
        return buildAuthResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Authenticated user missing from DB"));
        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    @Override
    public void logout(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return;
        }
        String token = bearerToken.substring(7);
        try {
            Claims claims = jwtService.extractClaim(token, c -> c);
            tokenBlacklist.blacklist(token, claims.getExpiration().getTime());
            log.info("Token blacklisted for subject={}", claims.getSubject());
        } catch (Exception ex) {
            log.warn("Logout with invalid token ignored");
        }
    }

    private Role resolveRole(String requested) {
        if (requested == null || requested.isBlank()) {
            return Role.USER;
        }
        try {
            return Role.valueOf(requested.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return Role.USER;
        }
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
