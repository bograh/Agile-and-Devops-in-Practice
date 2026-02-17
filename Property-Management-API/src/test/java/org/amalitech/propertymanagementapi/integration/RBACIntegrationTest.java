package org.amalitech.propertymanagementapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.amalitech.propertymanagementapi.dto.LoginRequest;
import org.amalitech.propertymanagementapi.model.Role;
import org.amalitech.propertymanagementapi.model.User;
import org.amalitech.propertymanagementapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RBACIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userToken;
    private String agentToken;
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        // Create users with different roles
        createUserWithRole("user@test.com", Role.USER);
        createUserWithRole("agent@test.com", Role.AGENT);
        createUserWithRole("admin@test.com", Role.ADMIN);

        // Get tokens for each role
        userToken = getTokenForUser("user@test.com", "password123");
        agentToken = getTokenForUser("agent@test.com", "password123");
        adminToken = getTokenForUser("admin@test.com", "password123");
    }

    private void createUserWithRole(String email, Role role) {
        if (!userRepository.existsByEmail(email)) {
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode("password123"))
                    .role(role)
                    .build();
            userRepository.save(user);
        }
    }

    private String getTokenForUser(String email, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(email, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("token").asText();
    }

    // Test: USER should NOT access ADMIN endpoints
    @Test
    @DisplayName("USER role should get 403 when accessing admin endpoint")
    void testUserCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/stats")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    // Test: AGENT should NOT access ADMIN endpoints
    @Test
    @DisplayName("AGENT role should get 403 when accessing admin endpoint")
    void testAgentCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/stats")
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isForbidden());
    }

    // Test: ADMIN should access ADMIN endpoints
    @Test
    @DisplayName("ADMIN role should get 200 when accessing admin endpoint")
    void testAdminCanAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/stats")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // Test: USER should NOT access AGENT endpoints
    @Test
    @DisplayName("USER role should get 403 when accessing agent endpoint")
    void testUserCannotAccessAgentEndpoint() throws Exception {
        mockMvc.perform(get("/api/agent/dashboard")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    // Test: AGENT should access AGENT endpoints
    @Test
    @DisplayName("AGENT role should get 200 when accessing agent endpoint")
    void testAgentCanAccessAgentEndpoint() throws Exception {
        mockMvc.perform(get("/api/agent/dashboard")
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    // Test: ADMIN should access AGENT endpoints (higher privilege)
    @Test
    @DisplayName("ADMIN role should get 200 when accessing agent endpoint")
    void testAdminCanAccessAgentEndpoint() throws Exception {
        mockMvc.perform(get("/api/agent/dashboard")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }
}
