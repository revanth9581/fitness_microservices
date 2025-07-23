package com.fitnessApp.activityService.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fitnessApp.activityService.model.ActivityType;

import lombok.Data;

@Data
public class ActivityRequest {
    private String userid;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurn;
    private LocalDateTime starTime;
    private Map<String,Object> additionalMetrics;


}
