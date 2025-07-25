package com.fitnessApp.activityService.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fitnessApp.activityService.model.Activity;

@Repository
public interface ActivityRepository extends MongoRepository<Activity,String>{

    List<Activity> findByUserid(String userid);

}
