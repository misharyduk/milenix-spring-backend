package com.project.milenix.user_service.user.service;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.service.ArticleDevService;
import com.project.milenix.user_service.exception.UsernameNotUniqueException;
import com.project.milenix.user_service.user.dto.*;
import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.exception.EmailNotUniqueException;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.user_service.user.repo.UserRepository;
import com.project.milenix.user_service.util.UserPaginationParametersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService extends UserServiceCommon {

    private final UserRepository userRepository;
    private final ArticleDevService articleDevService;
    private final UserPaginationParametersValidator paramsValidator;
    private final PasswordEncoder passwordEncoder;

    public User getUser(Integer id) throws CustomUserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomUserException("Cannot find user"));
    }

    public EntityUserResponseDto getUserById(Integer id, PaginationParameters paginationParameters) throws CustomUserException {

        paginationParameters.setField(paramsValidator.getCorrectValue(paginationParameters.getField()).getHqlField());

        // TODO: add custom exception
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomUserException("User is not found"));

        user.setPage(articleDevService.getArticlesPageByAuthor(
                id,
                paginationParameters
        ));

        return mapToDto(user);
    }

    public EntityUserResponseDto getUserByUsername(String username) throws CustomUserException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomUserException("User is not found"));

        user.setPage(articleDevService.getArticlesPageByAuthor(
                user.getId(),
                PaginationParameters.builder()
                        .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
        ));

        return mapToDto(user);
    }

    public EntityUserResponseDto getUserByEmail(String email) throws CustomUserException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomUserException("User is not found"));

        user.setPage(articleDevService.getArticlesPageByAuthor(
                user.getId(),
                PaginationParameters.builder()
                        .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
        ));

        return mapToDto(user);
    }


    public List<EntityUserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .peek(user -> user.setPage(articleDevService.getArticlesPageByAuthor(
                        user.getId(),
                        PaginationParameters.builder()
                                .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
                )))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<EntityUserResponseDto> findUsersWithSort(String field, String dirVal) {

        field = field == null ? "id" : field;
        field = paramsValidator.getCorrectValue(field).getHqlField();

        Sort.Direction direction = (dirVal != null && dirVal.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        return userRepository.findAll(Sort.by(direction, field)).stream()
                .peek(user -> user.setPage(articleDevService.getArticlesPageByAuthor(
                        user.getId(),
                        PaginationParameters.builder()
                                .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
                )))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Integer saveUser(UserRequestDto userRequest, String role, String imageName) throws EmailNotUniqueException, UsernameNotUniqueException {

        // TODO if password is not easy
        User user = User.builder()
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .image(imageName)
                .role(role == null ? "USER" : role.toUpperCase())
                .isAccountNonExpired(true) // TODO these things should be after email verification
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        checkEmailForUnique(user.getEmail());
        checkUsernameForUnique(user.getUsername());

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    public Integer registerUser(UserRequestDto userRequest, String imageName) throws EmailNotUniqueException, UsernameNotUniqueException {

        // TODO if password is not easy
        User user = User.builder()
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .image(imageName)
                .role("USER")
                .isAccountNonExpired(false) // TODO these things should be after email verification
                .isAccountNonLocked(false)
                .isCredentialsNonExpired(false)
                .isEnabled(false)
                .build();

        checkEmailForUnique(user.getEmail());
        checkUsernameForUnique(user.getUsername());

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    public EntityUserResponseDto updateUser(Integer id, UserRequestDto userRequestDto, MultipartFile image) throws CustomUserException, EmailNotUniqueException {
        User fetchedUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomUserException("User is not found"));

        String firstName = userRequestDto.getFirstName();
        if(firstName != null && !firstName.isBlank())
            fetchedUser.setFirstName(firstName);

        String lastName = userRequestDto.getLastName();
        if(lastName != null && !lastName.isBlank())
            fetchedUser.setLastName(lastName);
        String email = userRequestDto.getEmail();
        if(email != null && !email.isBlank())
            fetchedUser.setEmail(email);

        if(image != null)
          fetchedUser.setImage(image.getOriginalFilename());

        if (userRequestDto.getPassword() != null) {
            // TODO if password is not the same and not easy
            fetchedUser.setPassword(userRequestDto.getPassword());
        }

        checkEmailForUnique(fetchedUser.getEmail());

        userRepository.saveAndFlush(fetchedUser);

        fetchedUser.setPage(articleDevService.getArticlesPageByAuthor(
                fetchedUser.getId(),
                PaginationParameters.builder()
                        .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
        ));

        return mapToDto(fetchedUser);
    }

    public boolean deleteUser(Integer id) throws CustomUserException {
        if(userRepository.findById(id).isEmpty()){
            throw new CustomUserException(String.format("User with id %d cannot be found", id));
        }
        userRepository.deleteById(id);
        return userRepository.findById(id).isEmpty();
    }



    public EntityUserResponseDto getUserWithLikedArticles(Integer id, PaginationParameters paginationParameters) throws CustomUserException {

        paginationParameters.setField(paramsValidator.getCorrectValue(paginationParameters.getField()).getHqlField());

        // TODO: add custom exception
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomUserException(String.format("User with id %d cannot be found", id)));

        user.setPage(articleDevService.getLikedArticlesPageByUser(
                id,
                paginationParameters
        ));

        return mapToDto(user);
    }

    public EntityUserResponseDto getUserWithBookmarkedArticles(Integer id, PaginationParameters paginationParameters) throws CustomUserException {

        paginationParameters.setField(paramsValidator.getCorrectValue(paginationParameters.getField()).getHqlField());

        // TODO: add custom exception
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomUserException(String.format("User with id %d cannot be found", id)));

        user.setPage(articleDevService.getBookmarkedArticlesPageByUser(
                id,
                paginationParameters
        ));

        return mapToDto(user);
    }

    private void checkEmailForUnique(String email) throws EmailNotUniqueException {
        if(userRepository.findByEmail(email).isPresent()){
            throw new EmailNotUniqueException(String.format("User with email %s is already registered", email));
        }
    }

    private void checkUsernameForUnique(String username) throws UsernameNotUniqueException {
        if(userRepository.findByUsername(username).isPresent()){
            throw new UsernameNotUniqueException(String.format("User with username %s is already registered", username));
        }
    }

    public EntityUserResponseDto completeUser(Integer id, MultipartFile image) throws CustomUserException {
        User fetchedUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomUserException("User is not found"));

        if(image != null)
            fetchedUser.setImage(image.getOriginalFilename());

        userRepository.saveAndFlush(fetchedUser);

        return mapToDto(fetchedUser);
    }
}
