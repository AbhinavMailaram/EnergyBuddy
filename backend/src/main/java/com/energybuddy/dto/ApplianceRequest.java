package com.energybuddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplianceRequest {
    
    @NotBlank(message = "Appliance name is required")
    private String name;
    
    @NotNull(message = "Power rating is required")
    @Positive(message = "Power rating must be positive")
    private Double powerRatingWatts;
    
    private String category;
}
