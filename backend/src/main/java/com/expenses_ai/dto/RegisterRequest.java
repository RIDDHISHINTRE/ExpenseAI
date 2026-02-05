package com.expenses_ai.dto;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Email is invalid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6 ,message = "password must have at least 6 characters")
    private String password;

}
