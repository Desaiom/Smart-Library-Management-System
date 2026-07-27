package com.smartlibrary.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Registration payload. Demonstrates Jakarta Bean Validation annotations.
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be 6-64 characters")
    private String password;

    @Pattern(regexp = "^$|^[0-9]{10,15}$", message = "Phone must be 10-15 digits")
    private String phone;

    @Size(max = 255, message = "Address too long")
    private String address;

    /** Optional; defaults to USER server-side if null. */
    private String role;
}
