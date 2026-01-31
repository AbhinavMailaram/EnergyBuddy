package com.energybuddy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyLogRequest {
    
    @NotNull(message = "Appliance ID is required")
    private Long applianceId;
    
    @NotNull(message = "Usage hours is required")
    @Positive(message = "Usage hours must be positive")
    private Double usageHours;
    
    @NotNull(message = "Logged date is required")
    private LocalDate loggedDate;
}
