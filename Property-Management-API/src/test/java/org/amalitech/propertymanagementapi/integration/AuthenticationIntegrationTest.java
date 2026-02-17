package org.amalitech.propertymanagementapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.amalitech.propertymanagementapi.dto.LoginRequest;
import org.amalitech.propertymanagementapi.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Should login successfully with valid credentials and return JWT")
    void testLoginSuccess() throws Exception {
        // First register a user
        RegisterRequest registerRequest = new RegisterRequest("logintest@example.com", "password123");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));
        
        // Then login
        LoginRequest loginRequest = new LoginRequest("logintest@example.com", "password123");
        
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("logintest@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
    
    @Test
    @DisplayName("Should return 401 for invalid credentials")
    void testLoginInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "wrongpassword");
        
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }
    
    @Test
    @DisplayName("Should access secured endpoint with valid JWT token")
    void testSecuredEndpointWithValidToken() throws Exception {
        // Register and login to get token
        RegisterRequest registerRequest = new RegisterRequest("securetest@example.com", "password123");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));
        
        LoginRequest loginRequest = new LoginRequest("securetest@example.com", "password123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();
        
        String responseBody = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).get("token").asText();
        
        // Access secured endpoint
        mockMvc.perform(get("/api/test/secure")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Access granted to secured endpoint"))
                .andExpect(jsonPath("$.user").value("securetest@example.com"));
    }
    
    @Test
    @DisplayName("Should return 403 when accessing secured endpoint without token")
    void testSecuredEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/test/secure"))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @DisplayName("Should return 403 when accessing secured endpoint with invalid token")
    void testSecuredEndpointWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/test/secure")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isForbidden());
    }
}
