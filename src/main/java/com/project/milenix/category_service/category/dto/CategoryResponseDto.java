package com.project.milenix.category_service.category.dto;

import com.project.milenix.category_service.category.model.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDto {
    private Integer id;
    private String name;

    public CategoryResponseDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }
}