package com.fitness.aiservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.aiservice.model.Recomendation;
import com.fitness.aiservice.service.RecomendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recomendations")
public class RecomendationController {

    private final RecomendationService recomendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recomendation>> getUserRecomendation(@PathVariable String userId) {
        List<Recomendation> recomendations = recomendationService.getRecomendationsByUserId(userId);
        return ResponseEntity.ok(recomendations);

    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recomendation> getActivityRecomendation(@PathVariable String activityId) {
        Recomendation recomendations = recomendationService.getRecomendationsByActivityId(activityId);
        return ResponseEntity.ok(recomendations);
    }

}
