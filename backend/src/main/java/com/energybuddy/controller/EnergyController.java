package com.energybuddy.controller;

import com.energybuddy.dto.EnergyConsumptionResponse;
import com.energybuddy.dto.EnergyLogRequest;
import com.energybuddy.service.EnergyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/energy")
@CrossOrigin(origins = "http://localhost:3000")
public class EnergyController {

    @Autowired
    private EnergyService energyService;

    @PostMapping("/log")
    public ResponseEntity<?> logEnergyConsumption(@Valid @RequestBody EnergyLogRequest request) {
        try {
            EnergyConsumptionResponse response = energyService.logEnergyConsumption(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/consumption")
    public ResponseEntity<List<EnergyConsumptionResponse>> getUserConsumption() {
        return ResponseEntity.ok(energyService.getUserConsumption());
    }

    @GetMapping("/trends")
    public ResponseEntity<List<EnergyConsumptionResponse>> getTrends(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(energyService.getTrends(startDate, endDate));
    }
}
