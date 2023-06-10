package com.project.milenix.user_service.user.dto;

import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityUserResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String image;
    private ArticlePageResponseDto page;
}
