package org.amalitech.propertymanagementapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/secure")
    public ResponseEntity<Map<String, String>> secureEndpoint(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "message", "Access granted to secured endpoint",
                "user", authentication.getName()
        ));
    }
}
