
package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.exception.DuplicateEmailException;
import com.ecommerce.user_service.exception.InvalidCredentialsException;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.security.jwt.JwtService;
import com.ecommerce.user_service.service.AuthService;
import com.ecommerce.user_service.dto.auth.RegisterRequest;
import com.ecommerce.user_service.dto.auth.LoginRequest;
import com.ecommerce.user_service.dto.auth.AuthResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user.getEmail()), "Bearer");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
            return new AuthResponse(
                    jwtService.generateToken(authentication.getName()),
                    "Bearer");
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException();
        }
    }
}
