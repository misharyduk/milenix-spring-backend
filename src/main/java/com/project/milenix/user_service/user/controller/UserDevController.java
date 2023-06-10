package com.project.milenix.user_service.user.controller;

import com.project.milenix.user_service.exception.EmailNotUniqueException;
import com.project.milenix.user_service.user.dto.UserAuthResponseDto;
import com.project.milenix.user_service.user.dto.UserRequestDto;
import com.project.milenix.user_service.user.dto.UserResponseDto;
import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.user.service.UserDevService;
import com.project.milenix.user_service.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dev/api/v1/users")
@RequiredArgsConstructor
public class UserDevController {

    private final UserDevService userService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserResponse(@PathVariable("id") Integer id)  {
        return userService.getUserResponse(id);
    }

    @GetMapping(params = "email")
    public UserAuthResponseDto getByEmailToAuthenticate(@RequestParam("email") String email){
        return userService.getUserByEmailForAuth(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer registerUser(UserRequestDto user) throws EmailNotUniqueException {
        return userService.saveUser(user);
    }
}
