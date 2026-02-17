package org.amalitech.propertymanagementapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
@Slf4j
public class AgentController {
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        log.info("Agent/Admin {} accessing dashboard", authentication.getName());
        
        return ResponseEntity.ok(Map.of(
                "message", "Agent dashboard",
                "myProperties", 15,
                "pendingApprovals", 3,
                "accessedBy", authentication.getName()
        ));
    }
}
