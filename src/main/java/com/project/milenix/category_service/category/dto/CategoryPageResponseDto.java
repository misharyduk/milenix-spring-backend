package com.project.milenix.category_service.category.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryPageResponseDto { // MAIN RESPONSE DTO FOR LIST OF CATEGORIES
    private Long totalElements;
    private Integer totalPages;
    private List<EntityCategoryResponseDto> categories;
    private Integer page;
    private Integer pageSize;
}
