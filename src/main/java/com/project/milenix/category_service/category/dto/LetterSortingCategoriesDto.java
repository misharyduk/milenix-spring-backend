package com.project.milenix.category_service.category.dto;

import com.project.milenix.category_service.category.dto.CategoryResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LetterSortingCategoriesDto {
    private Character letter;
    private List<CategoryResponseDto> categories;
}
