
package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.auth.RegisterRequest;
import com.ecommerce.user_service.dto.auth.LoginRequest;
import com.ecommerce.user_service.dto.auth.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
    
}
 