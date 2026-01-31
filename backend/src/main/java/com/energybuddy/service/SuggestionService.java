package com.energybuddy.service;

import com.energybuddy.model.EnergyConsumption;
import com.energybuddy.model.User;
import com.energybuddy.repository.EnergyConsumptionRepository;
import com.energybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SuggestionService {

    @Autowired
    private EnergyConsumptionRepository energyConsumptionRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Map<String, Object>> generateEnergySavingSuggestions() {
        User user = getCurrentUser();
        List<Map<String, Object>> suggestions = new ArrayList<>();

        // Get last 30 days consumption
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        List<EnergyConsumption> recentConsumptions = energyConsumptionRepository
                .findByUserIdAndDateRange(user.getId(), startDate, endDate);

        if (recentConsumptions.isEmpty()) {
            Map<String, Object> suggestion = new HashMap<>();
            suggestion.put("title", "Start Logging Energy");
            suggestion.put("description", "Begin logging your appliance usage to receive personalized energy-saving suggestions.");
            suggestion.put("priority", "info");
            suggestions.add(suggestion);
            return suggestions;
        }

        // Calculate total consumption
        double totalKwh = recentConsumptions.stream()
                .mapToDouble(EnergyConsumption::getEnergyKwh)
                .sum();

        // Group by appliance and calculate consumption
        Map<String, Double> applianceConsumption = recentConsumptions.stream()
                .collect(Collectors.groupingBy(
                        ec -> ec.getAppliance().getName(),
                        Collectors.summingDouble(EnergyConsumption::getEnergyKwh)
                ));

        // Find high consumption appliances
        applianceConsumption.entrySet().stream()
                .filter(entry -> entry.getValue() > 50) // More than 50 kWh in 30 days
                .forEach(entry -> {
                    Map<String, Object> suggestion = new HashMap<>();
                    suggestion.put("title", "High Consumption Alert: " + entry.getKey());
                    suggestion.put("description", String.format(
                            "%s has consumed %.2f kWh in the last 30 days. Consider reducing usage time or upgrading to a more energy-efficient model.",
                            entry.getKey(), entry.getValue()
                    ));
                    suggestion.put("priority", "high");
                    suggestion.put("savings", String.format("Potential savings: %.2f kWh/month", entry.getValue() * 0.15));
                    suggestions.add(suggestion);
                });

        // General suggestions
        if (totalKwh > 200) {
            Map<String, Object> suggestion = new HashMap<>();
            suggestion.put("title", "Overall High Consumption");
            suggestion.put("description", String.format(
                    "Your total consumption is %.2f kWh in the last 30 days. Consider implementing energy-saving habits.",
                    totalKwh
            ));
            suggestion.put("priority", "medium");
            suggestions.add(suggestion);
        }

        // Peak usage suggestions
        Map<String, Object> suggestion = new HashMap<>();
        suggestion.put("title", "Optimize Usage Times");
        suggestion.put("description", "Try to use high-power appliances during off-peak hours to save on electricity costs.");
        suggestion.put("priority", "low");
        suggestions.add(suggestion);

        // Energy-efficient tips
        Map<String, Object> tip = new HashMap<>();
        tip.put("title", "Energy-Saving Tip");
        tip.put("description", "Unplug appliances when not in use to eliminate phantom power consumption.");
        tip.put("priority", "info");
        suggestions.add(tip);

        return suggestions;
    }
}
