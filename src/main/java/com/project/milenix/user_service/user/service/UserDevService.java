package com.project.milenix.user_service.user.service;

import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.exception.EmailNotUniqueException;
import com.project.milenix.user_service.user.dto.UserRequestDto;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.user_service.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDevService {

    private final UserRepository userRepository;

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

    public User getUser(Integer id) throws CustomUserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomUserException("Cannot find user"));
    }

    private void checkEmailForUnique(String email) throws EmailNotUniqueException {
        if(userRepository.findByEmail(email).isPresent()){
            throw new EmailNotUniqueException(String.format("User with email %s is already registered", email));
        }
    }

}
