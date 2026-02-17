package org.amalitech.propertymanagementapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.amalitech.propertymanagementapi.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Should successfully register a new user and return 201")
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest("newuser@example.com", "password123");
        
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
    
    @Test
    @DisplayName("Should return 409 when registering with duplicate email")
    void testRegisterDuplicateEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("duplicate@example.com", "password123");
        
        // Register first time
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        
        // Try to register again with same email
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email is already registered"))
                .andExpect(jsonPath("$.status").value(409));
    }
    
    @Test
    @DisplayName("Should return 400 for invalid email format")
    void testRegisterInvalidEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("invalid-email", "password123");
        
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
    
    @Test
    @DisplayName("Should return 400 for password less than 6 characters")
    void testRegisterShortPassword() throws Exception {
        RegisterRequest request = new RegisterRequest("user@example.com", "pass");
        
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
    
    @Test
    @DisplayName("Should return 400 for missing email")
    void testRegisterMissingEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("", "password123");
        
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
