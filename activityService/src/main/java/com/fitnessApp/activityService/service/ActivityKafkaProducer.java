package com.fitnessApp.activityService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import com.fitnessApp.activityService.model.Activity;

@Service
public class ActivityKafkaProducer {

    private KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    public ActivityKafkaProducer(KafkaTemplate<String, Activity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendActivity(Activity activity) {
        kafkaTemplate.send(topic,activity);
    }



}


