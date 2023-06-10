package com.project.milenix.category_service.category.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDto {
    private Integer id;
    private String name;
}