package com.smartlibrary.service;

import com.smartlibrary.dto.auth.AuthResponse;
import com.smartlibrary.dto.auth.LoginRequest;
import com.smartlibrary.dto.auth.RegisterRequest;

/**
 * Authentication operations: registration, login and logout (token invalidation).
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(String bearerToken);
}
