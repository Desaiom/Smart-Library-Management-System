package com.smartlibrary.service;

import com.smartlibrary.dto.user.UserPatchRequest;
import com.smartlibrary.dto.user.UserResponse;
import com.smartlibrary.dto.user.UserUpdateRequest;

import java.util.List;

public interface UserService {

    UserResponse getById(Long id);

    UserResponse getCurrentProfile();

    List<UserResponse> list();

    UserResponse update(Long id, UserUpdateRequest request);

    UserResponse patch(Long id, UserPatchRequest request);

    void delete(Long id);
}
