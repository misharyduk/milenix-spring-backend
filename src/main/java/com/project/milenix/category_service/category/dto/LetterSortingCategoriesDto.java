package com.project.milenix.category_service.category.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LetterSortingCategoriesDto {
    private Character letter;
    private List<EntityCategoryResponseDto> categories;
}
