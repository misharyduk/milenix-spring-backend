package com.project.milenix.article_service.article.controller;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.article_service.article.service.ArticlePaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articles/pagination")
@RequiredArgsConstructor
public class ArticlePaginationController {

    private final ArticlePaginationService articlePaginationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ArticlePageResponseDto getArticlesWithPagination(PaginationParameters params){
        return articlePaginationService.findArticlesWithPaginationAndSorting(params);
    }
}
