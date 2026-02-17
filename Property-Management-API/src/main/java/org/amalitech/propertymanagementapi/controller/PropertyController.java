package org.amalitech.propertymanagementapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitech.propertymanagementapi.dto.PropertyRequest;
import org.amalitech.propertymanagementapi.dto.PropertyResponse;
import org.amalitech.propertymanagementapi.model.User;
import org.amalitech.propertymanagementapi.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Slf4j
public class PropertyController {
    
    private final PropertyService propertyService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public ResponseEntity<PropertyResponse> createProperty(
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();
        PropertyResponse response = propertyService.createProperty(request, currentUser);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest request,
            Authentication authentication) {
        
        User currentUser = (User) authentication.getPrincipal();
        PropertyResponse response = propertyService.updateProperty(id, request, currentUser);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {
        List<PropertyResponse> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        PropertyResponse property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }
    
    @GetMapping("/my-properties")
    @PreAuthorize("hasAnyRole('AGENT', 'ADMIN')")
    public ResponseEntity<List<PropertyResponse>> getMyProperties(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<PropertyResponse> properties = propertyService.getUserProperties(currentUser);
        return ResponseEntity.ok(properties);
    }
}
