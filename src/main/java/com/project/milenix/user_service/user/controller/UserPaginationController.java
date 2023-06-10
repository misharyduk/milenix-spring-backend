package com.project.milenix.user_service.user.controller;

import com.project.milenix.PaginationParameters;
import com.project.milenix.user_service.user.dto.UserPageResponseDto;
import com.project.milenix.user_service.user.service.UserPaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/pagination")
@RequiredArgsConstructor
public class UserPaginationController {

    private final UserPaginationService userPaginationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserPageResponseDto findUsersWithPaginationAndSort(PaginationParameters params){
        return userPaginationService.findUserWithPaginationAndSort(params);
    }

}
