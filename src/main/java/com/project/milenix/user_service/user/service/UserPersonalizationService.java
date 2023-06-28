package com.project.milenix.user_service.user.service;

import com.project.milenix.authentication_service.util.JwtUtil;
import com.project.milenix.category_service.category.dto.CategoryRequestDto;
import com.project.milenix.category_service.category.model.Category;
import com.project.milenix.category_service.category.repo.CategoryRepository;
import com.project.milenix.category_service.exception.CategoryException;
import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.user_service.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserPersonalizationService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void saveUserCategories(Integer userId, List<CategoryRequestDto> categoryRequestDtos) {
        Set<Optional<Category>> categoriesOpt = categoryRequestDtos.stream()
                .map(c -> categoryRepository.findByName(c.getName()))
                .collect(Collectors.toSet());

        Set<Category> categories = new HashSet<>();
        for (Optional<Category> categoryOpt : categoriesOpt) {
            categoryOpt.ifPresent(categories::add);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No such user"));

        user.setInterestCategories(categories);
        userRepository.save(user);
    }

}
