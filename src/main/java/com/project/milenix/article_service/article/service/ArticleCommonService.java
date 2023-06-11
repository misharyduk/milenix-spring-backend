package com.project.milenix.article_service.article.service;

import com.project.milenix.article_service.article.model.Article;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;
import com.project.milenix.user_service.user.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ArticleCommonService {

    @Autowired
    private UserController userController;

    protected List<EntityArticleResponseDto> getListOfArticleDTOS(List<Article> articles) {

        for (Article article : articles) {
            if (article.getMainImagePath() != null) {
                article.setMainImagePath("article-images/" + article.getId() + "/" + article.getMainImagePath());
            }
        }

        return articles.stream()
                .map(EntityArticleResponseDto::new)
                .collect(Collectors.toList());
    }
}
