package com.energybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumptionResponse {
    private Long id;
    private Long applianceId;
    private String applianceName;
    private Double usageHours;
    private Double energyWh;
    private Double energyKwh;
    private LocalDate loggedDate;
    private Integer weekNumber;
    private Integer year;
}
