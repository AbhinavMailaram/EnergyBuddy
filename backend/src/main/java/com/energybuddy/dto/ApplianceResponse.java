package com.energybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplianceResponse {
    private Long id;
    private String name;
    private Double powerRatingWatts;
    private String category;
    private LocalDateTime createdAt;
}
