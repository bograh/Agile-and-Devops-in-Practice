package org.amalitech.propertymanagementapi.security;

import org.amalitech.propertymanagementapi.model.Role;
import org.amalitech.propertymanagementapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
        "jwt.expiration=86400000"
})
class JwtTokenProviderTest {
   
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    private UserDetails userDetails;
    
    @BeforeEach
    void setUp() {
        userDetails = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }
    
    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken() {
        String token = jwtTokenProvider.generateToken(userDetails, "USER");
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }
    
    @Test
    @DisplayName("Should extract username from token")
    void testExtractUsername() {
        String token = jwtTokenProvider.generateToken(userDetails, "USER");
        String extractedUsername = jwtTokenProvider.extractUsername(token);
        
        assertEquals("test@example.com", extractedUsername);
    }
    
    @Test
    @DisplayName("Should extract role from token")
    void testExtractRole() {
        String token = jwtTokenProvider.generateToken(userDetails, "USER");
        String extractedRole = jwtTokenProvider.extractRole(token);
        
        assertEquals("USER", extractedRole);
    }
    
    @Test
    @DisplayName("Should validate token successfully")
    void testValidateToken() {
        String token = jwtTokenProvider.generateToken(userDetails, "USER");
        Boolean isValid = jwtTokenProvider.validateToken(token, userDetails);
        
        assertTrue(isValid);
    }
    
    @Test
    @DisplayName("Should invalidate token with wrong user")
    void testInvalidateTokenWrongUser() {
        String token = jwtTokenProvider.generateToken(userDetails, "USER");
        
        UserDetails wrongUser = User.builder()
                .id(2L)
                .email("wrong@example.com")
                .password("password")
                .role(Role.USER)
                .build();
        
        Boolean isValid = jwtTokenProvider.validateToken(token, wrongUser);
        
        assertFalse(isValid);
    }
    
    @Test
    @DisplayName("Should extract expiration date")
    void testExtractExpiration() {
        String token = jwtTokenProvider.generateToken(userDetails, "USER");
        assertNotNull(jwtTokenProvider.extractExpiration(token));
    }
}
