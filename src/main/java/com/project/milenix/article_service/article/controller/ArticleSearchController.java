package com.project.milenix.article_service.article.controller;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.article_service.article.service.ArticleSearchService;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles/search")
@RequiredArgsConstructor
public class ArticleSearchController {

    private final ArticleSearchService articleSearchService;

    @GetMapping(path = "all")
    @ResponseStatus(HttpStatus.OK)
    public List<EntityArticleResponseDto> searchArticles(@RequestParam("value") String value,
                                                         @RequestParam(value = "field", required = false) String field,
                                                         @RequestParam(value = "direction", required = false) String direction){
        return articleSearchService.searchArticles(value, field, direction);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ArticlePageResponseDto searchArticlesWithPagination(@RequestParam("value") String value, PaginationParameters params){
        return articleSearchService.searchArticlesWithPagination(value, params);
    }
}
