package com.project.milenix.user_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "First name shouldn't be empty")
    private String firstName;
    @NotBlank(message = "Last name shouldn't be empty")
    private String lastName;
    @NotBlank(message = "Email shouldn't be empty")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password shouldn't be empty")
    private String password;
}