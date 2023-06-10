package com.project.milenix.article_service.article.controller;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.article_service.article.service.ArticleDevService;
import com.project.milenix.article_service.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev/api/v1")
@RequiredArgsConstructor
public class ArticleDevController {

    private final ArticleDevService articleService;

    @GetMapping(value = "articles/pagination",
            params = {"categoryId", "field", "page", "pageSize", "direction"})
    @ResponseStatus(HttpStatus.OK)
    public ArticlePageResponseDto getArticleResponsesByCategoryWithPagination(
            @RequestParam("categoryId") Integer categoryId,
            PaginationParameters paginationParameters
    ){
        return articleService.getArticlesPageByCategory(categoryId, paginationParameters);
    }

    @GetMapping(value = "articles/pagination",
            params = {"userId", "field", "page", "pageSize", "direction"})
    @ResponseStatus(HttpStatus.OK)
    public ArticlePageResponseDto getArticleResponsesByUserWithPagination(
            @RequestParam("userId") Integer userId,
            PaginationParameters paginationParameters
    ){
        return articleService.getArticlesPageByAuthor(userId, paginationParameters);
    }

    @GetMapping(value = "articles/likes",
            params = {"userId", "field", "page", "pageSize", "direction"})
    @ResponseStatus(HttpStatus.OK)
    public ArticlePageResponseDto getLikedArticlesByUserWithPagination(
            @RequestParam("userId") Integer userId,
            PaginationParameters paginationParameters
    ){
        return articleService.getLikedArticlesPageByUser(userId, paginationParameters);
    }

    @GetMapping(value = "articles/bookmarks",
            params = {"userId", "field", "page", "pageSize", "direction"})
    @ResponseStatus(HttpStatus.OK)
    public ArticlePageResponseDto getBookmarkedArticlesByUserWithPagination(
            @RequestParam("userId") Integer userId,
            PaginationParameters paginationParameters
    ){
        return articleService.getBookmarkedArticlesPageByUser(userId, paginationParameters);
    }

}
