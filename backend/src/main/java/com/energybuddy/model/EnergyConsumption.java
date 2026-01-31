package com.energybuddy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "energy_consumption")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appliance_id", nullable = false)
    private Appliance appliance;

    @NotNull(message = "Usage hours is required")
    @Positive(message = "Usage hours must be positive")
    @Column(name = "usage_hours", nullable = false)
    private Double usageHours;

    @Column(name = "energy_wh", nullable = false)
    private Double energyWh;

    @Column(name = "energy_kwh", nullable = false)
    private Double energyKwh;

    @NotNull(message = "Logged date is required")
    @Column(name = "logged_date", nullable = false)
    private LocalDate loggedDate;

    @Column(name = "week_number")
    private Integer weekNumber;

    @Column(name = "log_year")
    private Integer year;

    @PrePersist
    @PreUpdate
    protected void calculateEnergy() {
        if (appliance != null && usageHours != null) {
            // E = P × t (Energy = Power × time)
            this.energyWh = appliance.getPowerRatingWatts() * usageHours;
            this.energyKwh = this.energyWh / 1000.0;
        }
    }
}
