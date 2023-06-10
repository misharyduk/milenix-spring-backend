package com.project.milenix.category_service.category.controller;

import com.project.milenix.category_service.category.dto.CategoryPageResponseDto;
import com.project.milenix.category_service.category.dto.EntityCategoryResponseDto;
import com.project.milenix.category_service.category.service.CategorySearchService;
import com.project.milenix.PaginationParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories/search")
@RequiredArgsConstructor
public class CategorySearchController {

    private final CategorySearchService categorySearchService;

    @GetMapping(path = "all")
    @ResponseStatus(HttpStatus.OK)
    public List<EntityCategoryResponseDto> searchCategories(@RequestParam("value") String value,
                                                            @RequestParam(value = "field", required = false) String field,
                                                            @RequestParam(value = "direction", required = false) String direction){
        return categorySearchService.searchCategories(value, field, direction);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CategoryPageResponseDto searchCategories(@RequestParam("value") String value,
                                                    PaginationParameters params){
        return categorySearchService.searchCategoriesWithPagination(value, params);
    }
}
