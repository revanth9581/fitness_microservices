package com.fitnessApp.activityService.service;

import java.util.List;
import java.util.stream.Collectors;

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
    private final UserActivityService userActivityService;

    public  ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userActivityService.validateUser(request.getUserid());
        if (!isValidUser) { 
            throw new RuntimeException("Invalid user ID: " + request.getUserid());
        }

        
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

    public List<ActivityResponse> getUserActivities(String userid) {

        List<Activity> activities = activityRepository.findByUserid(userid);
        return activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());


    }

    public ActivityResponse getActivityById(String ActivityId) {
        return activityRepository.findById(ActivityId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + ActivityId));
    }

}
