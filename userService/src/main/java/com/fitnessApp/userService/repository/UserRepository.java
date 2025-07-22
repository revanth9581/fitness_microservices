package com.fitnessApp.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fitnessApp.userService.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

    boolean existsByEmail(String email);

}
