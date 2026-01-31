package com.energybuddy.service;

import com.energybuddy.dto.EnergyConsumptionResponse;
import com.energybuddy.dto.EnergyLogRequest;
import com.energybuddy.model.Appliance;
import com.energybuddy.model.EnergyConsumption;
import com.energybuddy.model.User;
import com.energybuddy.repository.ApplianceRepository;
import com.energybuddy.repository.EnergyConsumptionRepository;
import com.energybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class EnergyService {

    @Autowired
    private EnergyConsumptionRepository energyConsumptionRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public EnergyConsumptionResponse logEnergyConsumption(EnergyLogRequest request) {
        User user = getCurrentUser();
        
        Appliance appliance = applianceRepository.findByIdAndUserId(request.getApplianceId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Appliance not found"));

        EnergyConsumption consumption = new EnergyConsumption();
        consumption.setAppliance(appliance);
        consumption.setUsageHours(request.getUsageHours());
        consumption.setLoggedDate(request.getLoggedDate());

        // Calculate week number and year
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        consumption.setWeekNumber(request.getLoggedDate().get(weekFields.weekOfWeekBasedYear()));
        consumption.setYear(request.getLoggedDate().getYear());

        // Energy will be calculated automatically by @PrePersist
        consumption = energyConsumptionRepository.save(consumption);

        // Check for high consumption and create notification
        checkAndNotifyHighConsumption(consumption, user);

        return mapToResponse(consumption);
    }

    public List<EnergyConsumptionResponse> getUserConsumption() {
        User user = getCurrentUser();
        List<EnergyConsumption> consumptions = energyConsumptionRepository.findByUserId(user.getId());
        return consumptions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<EnergyConsumptionResponse> getTrends(LocalDate startDate, LocalDate endDate) {
        User user = getCurrentUser();
        
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<EnergyConsumption> consumptions = energyConsumptionRepository
                .findByUserIdAndDateRange(user.getId(), startDate, endDate);
        
        return consumptions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private void checkAndNotifyHighConsumption(EnergyConsumption consumption, User user) {
        // Threshold: If daily consumption exceeds 5 kWh, create notification
        if (consumption.getEnergyKwh() != null && consumption.getEnergyKwh() > 5.0) {
            String message = String.format(
                    "High energy consumption detected! %s consumed %.2f kWh on %s",
                    consumption.getAppliance().getName(),
                    consumption.getEnergyKwh(),
                    consumption.getLoggedDate()
            );
            notificationService.createNotification(user, message, "HIGH_CONSUMPTION");
        }
    }

    private EnergyConsumptionResponse mapToResponse(EnergyConsumption consumption) {
        return new EnergyConsumptionResponse(
                consumption.getId(),
                consumption.getAppliance().getId(),
                consumption.getAppliance().getName(),
                consumption.getUsageHours(),
                consumption.getEnergyWh(),
                consumption.getEnergyKwh(),
                consumption.getLoggedDate(),
                consumption.getWeekNumber(),
                consumption.getYear()
        );
    }
}
