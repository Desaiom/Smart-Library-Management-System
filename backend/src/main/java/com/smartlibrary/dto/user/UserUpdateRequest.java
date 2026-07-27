package com.smartlibrary.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Full update (PUT) of a user's editable fields.
 */
@Data
public class UserUpdateRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @Pattern(regexp = "^$|^[0-9]{10,15}$", message = "Phone must be 10-15 digits")
    private String phone;

    @Size(max = 255)
    private String address;
}
