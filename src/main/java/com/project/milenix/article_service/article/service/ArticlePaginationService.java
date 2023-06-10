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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticlePaginationService extends ArticleCommonService{

    private final ArticleRepository articleRepository;
    private final ArticlePaginationParametersValidator paramsValidator;

    public ArticlePageResponseDto findArticlesWithPaginationAndSorting(PaginationParameters params){

        params.setField(paramsValidator.getCorrectValue(params.getField()).getHqlField());

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<Article> pageOfArticles = articleRepository.findAll(
                PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(Sort.by(direction, params.getField())));

        List<Article> articles = pageOfArticles.stream().collect(Collectors.toList());

        List<EntityArticleResponseDto> articleDtos = getListOfArticleDTOS(articles);

        return ArticlePageResponseDto.builder()
                .totalElements(pageOfArticles.getTotalElements())
                .totalPages(pageOfArticles.getTotalPages())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .articles(articleDtos)
                .build();
    }

}
