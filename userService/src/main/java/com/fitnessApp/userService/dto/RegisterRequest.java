package com.fitnessApp.userService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterRequest {

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password must required")
    @Size(min = 6,message = "pass must be atleast 6 chars ")
    private String password;
    private String firstName;
    private String lastName;
}
