package com.project.milenix.article_service.article.dto;

//import com.project.milenix.category_service.category.dto.CategoryResponseDto;
//import com.project.milenix.user_service.user.dto.UserResponseDto;
import com.project.milenix.category_service.category.dto.EntityCategoryResponseDto;
import com.project.milenix.user_service.user.dto.EntityUserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityArticleResponseDto {
    private Integer id;
    private String title;
    private String content;
    private String mainImagePath;
    private String imagesDir;
    private LocalDateTime publishingDate;
    private Integer minutesToRead;
    private Integer numberOfViews = 0;
    private Integer numberOfLikes = 0;
    private EntityUserResponseDto author;
    private EntityCategoryResponseDto category;
}
