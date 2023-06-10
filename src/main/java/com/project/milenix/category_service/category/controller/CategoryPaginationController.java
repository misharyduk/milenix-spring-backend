package com.project.milenix.category_service.category.controller;

import com.project.milenix.category_service.category.dto.CategoryPageResponseDto;
import com.project.milenix.category_service.category.service.CategoryPaginationService;
import com.project.milenix.PaginationParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories/pagination")
@RequiredArgsConstructor
public class CategoryPaginationController {

    private final CategoryPaginationService categoryPaginationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CategoryPageResponseDto getCategoriesWithPaginationAndSort(PaginationParameters params){
        return categoryPaginationService.findCategoriesWithPaginationAndSorting(params);
    }
}
