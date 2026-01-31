package com.energybuddy.controller;

import com.energybuddy.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin(origins = "http://localhost:3000")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSuggestions() {
        return ResponseEntity.ok(suggestionService.generateEnergySavingSuggestions());
    }
}
