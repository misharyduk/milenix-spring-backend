package com.project.milenix.user_service.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}