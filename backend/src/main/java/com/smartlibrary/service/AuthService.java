package com.smartlibrary.service;

import com.smartlibrary.dto.auth.*;

/**
 * Authentication operations: registration, login and logout (token invalidation).
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void logout(String bearerToken);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
