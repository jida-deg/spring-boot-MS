package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.user.UpdateUserRequest;
import com.ecommerce.user_service.dto.user.UserResponse;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);
}