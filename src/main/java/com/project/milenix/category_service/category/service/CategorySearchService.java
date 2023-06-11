package com.project.milenix.category_service.category.service;

import com.project.milenix.article_service.article.service.ArticleDevService;
import com.project.milenix.category_service.category.dto.CategoryPageResponseDto;
import com.project.milenix.category_service.category.dto.EntityCategoryResponseDto;
import com.project.milenix.category_service.category.model.Category;
import com.project.milenix.category_service.category.repo.CategoryRepository;
import com.project.milenix.PaginationParameters;
import com.project.milenix.category_service.util.CategoryPaginationParametersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorySearchService {

    private final CategoryRepository categoryRepository;
    private final CategoryPaginationParametersValidator paramsValidator;
    private final ArticleDevService articleDevService;

    public List<EntityCategoryResponseDto> searchCategories(String value, String fieldVal, String dirVal){

        if(value.isBlank()){
            return new ArrayList<>();
        }

        fieldVal = (fieldVal == null) ? "id" : fieldVal;
        String field = paramsValidator.getCorrectValue(fieldVal);


        Sort.Direction direction = (dirVal != null && dirVal.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;


        return categoryRepository.searchCategories(value, Sort.by(direction, field)).stream()
                .peek(category -> category.setPage(articleDevService.getArticlesPageByCategory(
                        category.getId(),
                        PaginationParameters.builder()
                                .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
                )))
                .map(EntityCategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    public CategoryPageResponseDto searchCategoriesWithPagination(String value, PaginationParameters params){

        if(value.isBlank()){
            return new CategoryPageResponseDto();
        }

        params.setField(paramsValidator.getCorrectValue(params.getField()));

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<Category> categoriesPage = categoryRepository.searchCategoriesWithPagination(
                value, PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(direction, params.getField()));

        List<EntityCategoryResponseDto> categories = categoriesPage.stream()
                .peek(category -> category.setPage(articleDevService.getArticlesPageByCategory(
                        category.getId(),
                        PaginationParameters.builder()
                                .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
                )))
                .map(EntityCategoryResponseDto::new)
                .collect(Collectors.toList());
        return CategoryPageResponseDto.builder()
                .totalElements(categoriesPage.getTotalElements())
                .totalPages(categoriesPage.getTotalPages())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .categories(categories)
                .build();
    }

}
