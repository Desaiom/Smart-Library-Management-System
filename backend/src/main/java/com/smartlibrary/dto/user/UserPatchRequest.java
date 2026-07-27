package com.smartlibrary.dto.user;

import lombok.Data;

/**
 * Partial update (PATCH). All fields optional; only non-null fields are applied.
 * Used by ADMIN to change a user's role, for example.
 */
@Data
public class UserPatchRequest {
    private String name;
    private String phone;
    private String address;
    private String role;
}
