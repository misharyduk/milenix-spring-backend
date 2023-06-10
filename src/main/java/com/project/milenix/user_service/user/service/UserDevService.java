package com.project.milenix.user_service.user.service;

import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.exception.EmailNotUniqueException;
import com.project.milenix.user_service.user.dto.UserAuthResponseDto;
import com.project.milenix.user_service.user.dto.UserRequestDto;
import com.project.milenix.user_service.user.dto.UserResponseDto;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.user_service.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDevService {

    private final UserRepository userRepository;

    public UserAuthResponseDto getUserByEmailForAuth(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User is not found"));

        return UserAuthResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public UserResponseDto getUserResponse(Integer id)   {
        User userInDb = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("User with id %d cannot be found", id)));
        return UserResponseDto.builder()
                .id(userInDb.getId())
                .firstName(userInDb.getFirstName())
                .lastName(userInDb.getLastName())
                .build();
    }

    public Integer saveUser(UserRequestDto userRequest) throws EmailNotUniqueException {

        // TODO if password is not easy
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .role("USER")
                .build();

        checkEmailForUnique(user.getEmail());

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    private void checkEmailForUnique(String email) throws EmailNotUniqueException {
        if(userRepository.findByEmail(email).isPresent()){
            throw new EmailNotUniqueException(String.format("User with email %s is already registered", email));
        }
    }

}
