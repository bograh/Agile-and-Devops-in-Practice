package org.amalitech.propertymanagementapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.amalitech.propertymanagementapi.dto.PropertyRequest;
import org.amalitech.propertymanagementapi.dto.PropertyResponse;
import org.amalitech.propertymanagementapi.exception.PropertyNotFoundException;
import org.amalitech.propertymanagementapi.model.Property;
import org.amalitech.propertymanagementapi.model.User;
import org.amalitech.propertymanagementapi.repository.PropertyRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {
    
    private final PropertyRepository propertyRepository;
    
    @Transactional
    public PropertyResponse createProperty(PropertyRequest request, User owner) {
        log.info("Creating property '{}' for user {}", request.getTitle(), owner.getEmail());
        
        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .owner(owner)
                .build();
        
        Property savedProperty = propertyRepository.save(property);
        log.info("Property created with id: {}", savedProperty.getId());
        
        return mapToResponse(savedProperty);
    }
    
    @Transactional
    public PropertyResponse updateProperty(Long propertyId, PropertyRequest request, User currentUser) {
        log.info("Updating property {} by user {}", propertyId, currentUser.getEmail());
        
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with id: " + propertyId));
        
        // Validate ownership
        if (!property.getOwner().getId().equals(currentUser.getId())) {
            log.warn("User {} attempted to edit property {} owned by another user", 
                    currentUser.getEmail(), propertyId);
            throw new AccessDeniedException("You can only edit your own properties");
        }
        
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        
        Property updatedProperty = propertyRepository.save(property);
        log.info("Property {} updated successfully", propertyId);
        
        return mapToResponse(updatedProperty);
    }
    
    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllProperties() {
        log.info("Fetching all properties");
        return propertyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(Long id) {
        log.info("Fetching property with id: {}", id);
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with id: " + id));
        
        return mapToResponse(property);
    }
    
    @Transactional(readOnly = true)
    public List<PropertyResponse> getUserProperties(User user) {
        log.info("Fetching properties for user {}", user.getEmail());
        return propertyRepository.findByOwnerId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private PropertyResponse mapToResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .price(property.getPrice())
                .ownerEmail(property.getOwner().getEmail())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .build();
    }
}
