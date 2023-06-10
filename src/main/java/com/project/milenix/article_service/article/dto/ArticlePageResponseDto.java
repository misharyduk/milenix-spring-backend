package com.project.milenix.article_service.article.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticlePageResponseDto {
    private Long totalElements;
    private Integer totalPages;
    private List<EntityArticleResponseDto> articles;
    private Integer page;
    private Integer pageSize;
}
