package org.amalitech.propertymanagementapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitech.propertymanagementapi.dto.AuthResponse;
import org.amalitech.propertymanagementapi.dto.LoginRequest;
import org.amalitech.propertymanagementapi.model.User;
import org.amalitech.propertymanagementapi.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthResponse login(LoginRequest request) {
        try {
            log.info("Attempting login for user: {}", request.getEmail());
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            User user = (User) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(user, user.getRole().name());
            
            log.info("User logged in successfully: {}", request.getEmail());
            
            return AuthResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
                    
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user: {}", request.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
