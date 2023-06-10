package com.project.milenix.category_service.category.controller;

import com.project.milenix.category_service.category.service.CategoryDevService;
import com.project.milenix.category_service.category.service.CategoryService;
import com.project.milenix.category_service.category.dto.CategoryResponseDto;
import com.project.milenix.category_service.exception.CategoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev/api/v1/categories")
@RequiredArgsConstructor
public class CategoryDevController {

    private final CategoryDevService categoryService;


    @GetMapping("{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto getCategoryResponse(@PathVariable("categoryId") Integer categoryId){
        return categoryService.getCategoryResponse(categoryId);
    }
}
