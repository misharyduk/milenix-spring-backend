package com.project.milenix.article_service.article.service;

import com.project.milenix.article_service.article.model.Article;
import com.project.milenix.article_service.article.repo.ArticleRepository;
import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;
import com.project.milenix.article_service.util.ArticlePaginationParametersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleSearchService extends ArticleCommonService{

    private final ArticleRepository articleRepository;
    private final ArticlePaginationParametersValidator paramsValidator;

    public List<EntityArticleResponseDto> searchArticles(String value, String fieldVal, String dirVal){

        if(value == null)
            return new ArrayList<>();

        Sort.Direction direction = (dirVal != null && dirVal.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        String field = fieldVal == null ? "id" : fieldVal;

        List<Article> articles = articleRepository.searchArticles(value, Sort.by(direction, field));
        return getListOfArticleDTOS(articles);
    }

    public ArticlePageResponseDto searchArticlesWithPagination(String value, PaginationParameters params) {

        if(value == null)
            return new ArticlePageResponseDto();

        params.setField(paramsValidator.getCorrectValue(params.getField()).name());

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<Article> articlesPage = articleRepository.searchArticlesWithPagination(
                value, PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(direction, params.getField()));

        List<Article> articlesList = articlesPage.stream().collect(Collectors.toList());

        List<EntityArticleResponseDto> articles = getListOfArticleDTOS(articlesList);

        return ArticlePageResponseDto.builder()
                .totalPages(articlesPage.getTotalPages())
                .totalElements(articlesPage.getTotalElements())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .articles(articles)
                .build();
    }
}
