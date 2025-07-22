package com.fitnessApp.userService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitnessApp.userService.dto.RegisterRequest;
import com.fitnessApp.userService.dto.UserResponse;
import com.fitnessApp.userService.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/{userID}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userID){
        return ResponseEntity.ok(userService.getUserProfile(userID));
    }

    @PostMapping("register")
    public ResponseEntity<UserResponse> regisetr(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

}
