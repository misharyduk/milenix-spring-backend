package com.project.milenix.article_service.article.service;

import com.project.milenix.article_service.article.model.Article;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;

import com.project.milenix.category_service.category.controller.CategoryDevController;
import com.project.milenix.category_service.category.dto.CategoryResponseDto;
import com.project.milenix.user_service.user.controller.UserController;
import com.project.milenix.user_service.user.controller.UserDevController;
import com.project.milenix.user_service.user.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ArticleCommonService {

    @Autowired
    private CategoryDevController categoryDevController;
    @Autowired
    private UserDevController userDevController;
    @Autowired
    private UserController userController;

    protected EntityArticleResponseDto mapToArticleDTO(Article article) {
        return EntityArticleResponseDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .mainImagePath(article.getMainImagePath())
                .imagesDir(article.getImagesDir())
                .publishingDate(article.getPublishingDate())
                .minutesToRead(article.getMinutesToRead())
                .numberOfViews(article.getNumberOfViews())
                .numberOfLikes(article.getNumberOfLikes())
                .category(article.getCategory())
                .author(article.getAuthor())
                .build();
    }

    protected List<EntityArticleResponseDto> getListOfArticleDTOS(List<Article> articles) {

        for (Article article : articles) {
            if (article.getMainImagePath() != null) {
                article.setMainImagePath("article-images/" + article.getId() + "/" + article.getMainImagePath());
            }
        }

        Map<Integer, CategoryResponseDto> categories = getMapWithCategoryIds(articles);

        var authors = getMapWithUserIds(articles);

        return articles.stream()
                .peek(article -> article.setCategory(categories.get(article.getCategoryId())))
                .peek(article -> article.setAuthor(authors.get(article.getAuthorId())))
                .map(this::mapToArticleDTO)
                .collect(Collectors.toList());
    }

    protected Map<Integer, UserResponseDto> getMapWithUserIds(List<Article> articles) {
        List<Integer> authorIds = new ArrayList<>();
        Map<Integer, UserResponseDto> collect = articles.stream()
                .filter(article -> !authorIds.contains(article.getAuthorId()))
                .peek(article -> authorIds.add(article.getAuthorId()))
                .map(article -> userDevController.getUserResponse(article.getAuthorId()))
                .collect(Collectors.toMap(UserResponseDto::getId, Function.identity()));
        Set<Map.Entry<Integer, UserResponseDto>> entries = collect.entrySet();
        return collect;
    }

    protected Map<Integer, CategoryResponseDto> getMapWithCategoryIds(List<Article> articles) {
        List<Integer> categoriesIds = new ArrayList<>();
        return articles.stream()
                .filter(article -> !categoriesIds.contains(article.getCategoryId()))
                .peek(article -> categoriesIds.add(article.getCategoryId()))
                .map(article -> categoryDevController.getCategoryResponse(article.getCategoryId()))
                .map(categoryResponseDto -> CategoryResponseDto.builder().id(categoryResponseDto.getId()).name(categoryResponseDto.getName()).build())
                .collect(Collectors.toMap(CategoryResponseDto::getId, Function.identity()));
    }

}
