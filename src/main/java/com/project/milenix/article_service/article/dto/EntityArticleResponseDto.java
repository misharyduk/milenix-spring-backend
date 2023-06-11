package com.project.milenix.article_service.article.dto;

//import com.project.milenix.category_service.category.dto.CategoryResponseDto;
//import com.project.milenix.user_service.user.dto.UserResponseDto;
import com.project.milenix.article_service.article.model.Article;
import com.project.milenix.category_service.category.dto.EntityCategoryResponseDto;
import com.project.milenix.user_service.user.dto.EntityUserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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

    public EntityArticleResponseDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.mainImagePath = article.getMainImagePath();
        this.imagesDir = article.getImagesDir();
        this.publishingDate = article.getPublishingDate();
        this.minutesToRead = article.getMinutesToRead();
        this.numberOfViews = article.getNumberOfViews();
        this.numberOfLikes = article.getNumberOfLikes();
        this.author = new EntityUserResponseDto(article.getAuthor());
        this.category = new EntityCategoryResponseDto(article.getCategory());
    }
}
