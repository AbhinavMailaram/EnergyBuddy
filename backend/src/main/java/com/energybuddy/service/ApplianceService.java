package com.energybuddy.service;

import com.energybuddy.dto.ApplianceRequest;
import com.energybuddy.dto.ApplianceResponse;
import com.energybuddy.model.Appliance;
import com.energybuddy.model.User;
import com.energybuddy.repository.ApplianceRepository;
import com.energybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplianceService {

    @Autowired
    private ApplianceRepository applianceRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ApplianceResponse createAppliance(ApplianceRequest request) {
        User user = getCurrentUser();

        Appliance appliance = new Appliance();
        appliance.setName(request.getName());
        appliance.setPowerRatingWatts(request.getPowerRatingWatts());
        appliance.setCategory(request.getCategory());
        appliance.setUser(user);

        appliance = applianceRepository.save(appliance);

        return mapToResponse(appliance);
    }

    public List<ApplianceResponse> getUserAppliances() {
        User user = getCurrentUser();
        List<Appliance> appliances = applianceRepository.findByUserId(user.getId());
        return appliances.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ApplianceResponse updateAppliance(Long id, ApplianceRequest request) {
        User user = getCurrentUser();
        Appliance appliance = applianceRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Appliance not found"));

        appliance.setName(request.getName());
        appliance.setPowerRatingWatts(request.getPowerRatingWatts());
        appliance.setCategory(request.getCategory());

        appliance = applianceRepository.save(appliance);

        return mapToResponse(appliance);
    }

    public void deleteAppliance(Long id) {
        User user = getCurrentUser();
        Appliance appliance = applianceRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Appliance not found"));

        applianceRepository.delete(appliance);
    }

    private ApplianceResponse mapToResponse(Appliance appliance) {
        return new ApplianceResponse(
                appliance.getId(),
                appliance.getName(),
                appliance.getPowerRatingWatts(),
                appliance.getCategory(),
                appliance.getCreatedAt()
        );
    }
}
