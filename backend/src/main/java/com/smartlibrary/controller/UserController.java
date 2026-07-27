package com.smartlibrary.controller;

import com.smartlibrary.dto.common.ApiResponse;
import com.smartlibrary.dto.user.UserPatchRequest;
import com.smartlibrary.dto.user.UserResponse;
import com.smartlibrary.dto.user.UserUpdateRequest;
import com.smartlibrary.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User administration. The whole {@code /users/**} space is ADMIN-only via
 * {@code SecurityConfig}, except {@code /users/me} which any authenticated user
 * may call to fetch their own profile.
 */
@Tag(name = "Users", description = "User administration")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "List all users (admin)")
//    @Operation(summary = "Get my own profile (any authenticated user)")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getCurrentProfile()));
    }

    @Operation(summary = "List all users (admin)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(userService.list()));
    }

    @Operation(summary = "Get a user by id (admin)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getById(id)));
    }

    @Operation(summary = "Full update of a user (admin)")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable Long id,
                                                            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User updated", userService.update(id, request)));
    }

    @Operation(summary = "Partial update (e.g. change role) (admin)")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> patch(@PathVariable Long id,
                                                           @RequestBody UserPatchRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User updated", userService.patch(id, request)));
    }

    @Operation(summary = "Delete a user (admin)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted", null));
    }
}
