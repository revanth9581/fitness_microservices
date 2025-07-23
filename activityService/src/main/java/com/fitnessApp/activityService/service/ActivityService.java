package com.fitnessApp.activityService.service;

import org.springframework.stereotype.Service;

import com.fitnessApp.activityService.dto.ActivityRequest;
import com.fitnessApp.activityService.dto.ActivityResponse;
import com.fitnessApp.activityService.model.Activity;
import com.fitnessApp.activityService.repository.ActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public  ActivityResponse trackActivity(ActivityRequest request) {

        Activity activity= Activity.builder()
        .userid(request.getUserid())
        .type(request.getType())
        .duration(request.getDuration())
        .caloriesBurned(request.getCaloriesBurn())
        .startTime(request.getStarTime())
        .additionalMetrics(request.getAdditionalMetrics())
        .build();

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);


        
    }

    public ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserid(activity.getUserid());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setStartTime(activity.getStartTime());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;
    }

}
