package com.project.milenix.category_service.category.dto;

import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityCategoryResponseDto {
    private Integer id;
    private String name;
//    private List<EntityArticleResponseDto> articles = new ArrayList<>();
    private ArticlePageResponseDto page;
}
