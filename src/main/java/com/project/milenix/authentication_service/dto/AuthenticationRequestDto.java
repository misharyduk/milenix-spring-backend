
package com.project.milenix.authentication_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDto {
    @NotBlank(message = "Email shouldn't be blank")
    @Email
    private String email;
    @NotBlank(message = "Password shouldn't be blank")
    private String password;
}