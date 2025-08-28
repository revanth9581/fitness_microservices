package com.fitness.aiservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recomendation;
import com.fitness.aiservice.repository.RecomendationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
@RequiredArgsConstructor
public class ActivityListenerKafka {

    private final ActivityAiService aiService;
    private final RecomendationRepository recomendationRepository;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "ai-service-group1")
    public void processActivity(Activity activity) {
        log.info(" Received activity from Kafka: " + activity.getId());
        // log.info("general recomendation :{}",aiService.generateRecommendation(activity));
        Recomendation recomendation = aiService.generateRecommendation(activity);
        recomendationRepository.save(recomendation);
    }
}

