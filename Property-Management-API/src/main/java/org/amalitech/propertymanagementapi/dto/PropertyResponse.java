package org.amalitech.propertymanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String ownerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
