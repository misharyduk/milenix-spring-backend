package com.project.milenix.user_service.user.controller;

import com.project.milenix.PaginationParameters;
import com.project.milenix.user_service.user.dto.EntityUserResponseDto;
import com.project.milenix.user_service.user.dto.UserPageResponseDto;
import com.project.milenix.user_service.user.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/search")
@RequiredArgsConstructor
public class UserSearchController {

    private final UserSearchService userSearchService;

    @GetMapping("all")
    @PreAuthorize("hasAuthority('user:read:all')")
    @ResponseStatus(HttpStatus.OK)
    public List<EntityUserResponseDto> searchUser(@RequestParam("value") String value,
                                                  @RequestParam(value = "field", required = false) String field,
                                                  @RequestParam(value = "direction", required = false) String direction){
        return userSearchService.searchUsers(value, field, direction);
    }

    @GetMapping
    public UserPageResponseDto searchUserWithPagination(@RequestParam("value") String value,
                                                        PaginationParameters params){
        return userSearchService.searchUserWithPagination(value, params);
    }

}
