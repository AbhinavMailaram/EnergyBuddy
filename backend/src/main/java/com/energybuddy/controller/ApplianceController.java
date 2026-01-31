package com.energybuddy.controller;

import com.energybuddy.dto.ApplianceRequest;
import com.energybuddy.dto.ApplianceResponse;
import com.energybuddy.service.ApplianceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appliances")
@CrossOrigin(origins = "http://localhost:3000")
public class ApplianceController {

    @Autowired
    private ApplianceService applianceService;

    @GetMapping
    public ResponseEntity<List<ApplianceResponse>> getAllAppliances() {
        return ResponseEntity.ok(applianceService.getUserAppliances());
    }

    @PostMapping
    public ResponseEntity<?> createAppliance(@Valid @RequestBody ApplianceRequest request) {
        try {
            ApplianceResponse response = applianceService.createAppliance(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppliance(@PathVariable Long id, @Valid @RequestBody ApplianceRequest request) {
        try {
            ApplianceResponse response = applianceService.updateAppliance(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppliance(@PathVariable Long id) {
        try {
            applianceService.deleteAppliance(id);
            return ResponseEntity.ok(Map.of("message", "Appliance deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
