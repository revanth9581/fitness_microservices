package com.fitness.aiservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Recomendation;
import com.fitness.aiservice.repository.RecomendationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecomendationService {

    private final RecomendationRepository recomendationRepository;

    public List<Recomendation> getRecomendationsByUserId(String userId) {
        return recomendationRepository.findByUserId(userId);

    }

    public Recomendation getRecomendationsByActivityId(String activityId) {
        return recomendationRepository.findByActivityId(activityId).
                                        orElseThrow(()-> new RuntimeException("Recomendation not found for activityId: " + activityId));

    }


}
