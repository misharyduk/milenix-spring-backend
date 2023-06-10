package com.project.milenix.category_service.category.service;

import com.project.milenix.article_service.article.controller.ArticleDevController;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryPaginationService extends CategoryCommonService{

    private final CategoryRepository categoryRepository;
    private final CategoryPaginationParametersValidator paramsValidator;
    private final ArticleDevController articleDevController;

    public CategoryPageResponseDto findCategoriesWithPaginationAndSorting(PaginationParameters params){

        params.setField(paramsValidator.getCorrectValue(params.getField()));

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<Category> categoriesPage = categoryRepository.findAll(
                PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(Sort.by(direction, params.getField())));

        List<Category> categories = categoriesPage.stream().collect(Collectors.toList());

        List<EntityCategoryResponseDto> categoryDtos = getListOfCategoryDTOS(categories);

        return CategoryPageResponseDto.builder()
                .totalElements(categoriesPage.getTotalElements())
                .totalPages(categoriesPage.getTotalPages())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .categories(categoryDtos)
                .build();
    }

    private List<EntityCategoryResponseDto> getListOfCategoryDTOS(List<Category> categories){
        return categories.stream()
                .peek(category -> category.setPage(articleDevController.getArticleResponsesByCategoryWithPagination(
                        category.getId(),
                        PaginationParameters.builder()
                                .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
                )))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
