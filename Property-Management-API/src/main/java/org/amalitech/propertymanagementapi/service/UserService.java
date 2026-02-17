package org.amalitech.propertymanagementapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitech.propertymanagementapi.dto.RegisterRequest;
import org.amalitech.propertymanagementapi.exception.DuplicateEmailException;
import org.amalitech.propertymanagementapi.model.Role;
import org.amalitech.propertymanagementapi.model.User;
import org.amalitech.propertymanagementapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public User register(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: email already exists - {}", request.getEmail());
            throw new DuplicateEmailException("Email is already registered");
        }
        
        // Create new user with default role
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id: {} and role: {}", savedUser.getId(), savedUser.getRole());
        
        return savedUser;
    }
}
