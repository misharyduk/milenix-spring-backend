package com.project.milenix.user_service.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPageResponseDto { // MAIN RESPONSE DTO FOR LIST OF USERS
    private Long totalElements;
    private Integer totalPages;
    private Integer page;
    private Integer pageSize;
    private List<EntityUserResponseDto> users;
}
