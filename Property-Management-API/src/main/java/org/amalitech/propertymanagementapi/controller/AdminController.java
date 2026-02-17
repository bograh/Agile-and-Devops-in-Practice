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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats(Authentication authentication) {
        log.info("Admin {} accessing system stats", authentication.getName());
        
        return ResponseEntity.ok(Map.of(
                "message", "Admin statistics",
                "totalUsers", 100,
                "totalProperties", 50,
                "totalAgents", 10,
                "accessedBy", authentication.getName()
        ));
    }
}
