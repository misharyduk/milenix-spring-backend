package com.project.milenix.category_service.category.service;

import com.project.milenix.category_service.category.dto.CategoryResponseDto;
import com.project.milenix.category_service.category.model.Category;
import com.project.milenix.category_service.category.repo.CategoryRepository;
import com.project.milenix.category_service.exception.CategoryException;
import com.project.milenix.user_service.exception.CustomUserException;
import com.project.milenix.user_service.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryDevService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto getCategoryResponse(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Category with id %d cannot be found", id)));
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category getCategory(Integer id) throws CustomUserException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find category"));
    }

}
