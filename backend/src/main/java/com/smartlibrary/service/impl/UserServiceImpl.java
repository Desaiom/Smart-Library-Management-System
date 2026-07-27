package com.smartlibrary.service.impl;

import com.smartlibrary.dto.user.UserPatchRequest;
import com.smartlibrary.dto.user.UserResponse;
import com.smartlibrary.dto.user.UserUpdateRequest;
import com.smartlibrary.entity.Role;
import com.smartlibrary.entity.User;
import com.smartlibrary.exception.InvalidOperationException;
import com.smartlibrary.exception.ResourceNotFoundException;
import com.smartlibrary.mapper.UserMapper;
import com.smartlibrary.repository.UserRepository;
import com.smartlibrary.security.SecurityUtils;
import com.smartlibrary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.securityUtils = securityUtils;
    }

    @Override
    public UserResponse getById(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    @Override
    public UserResponse getCurrentProfile() {
        return userMapper.toResponse(securityUtils.getCurrentUser());
    }

    @Override
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = findUser(id);
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse patch(Long id, UserPatchRequest request) {
        User user = findUser(id);
        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getRole() != null) {
            try {
                user.setRole(Role.valueOf(request.getRole().trim().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new InvalidOperationException("Invalid role: " + request.getRole());
            }
        }
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
        log.info("Deleted user id={}", id);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
}
