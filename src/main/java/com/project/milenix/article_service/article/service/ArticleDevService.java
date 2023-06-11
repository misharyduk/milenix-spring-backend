package com.project.milenix.article_service.article.service;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;
import com.project.milenix.article_service.article.model.Article;
import com.project.milenix.article_service.article.model.Bookmark;
import com.project.milenix.article_service.article.model.Like;
import com.project.milenix.article_service.article.repo.ArticleRepository;
import com.project.milenix.article_service.article.repo.BookmarkRepository;
import com.project.milenix.article_service.article.repo.LikeRepository;
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
public class ArticleDevService { // TODO: why not to extends ArticleCommonService?

    private final ArticlePaginationParametersValidator paramsValidator;
    private final ArticleRepository articleRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;

    public ArticlePageResponseDto getArticlesPageByCategory(Integer categoryId, PaginationParameters params) {

        params.setField(paramsValidator.getCorrectValue(params.getField()).getHqlField());

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<Article> pageOfArticles = articleRepository.findAllByCategoryId(
                categoryId,
                PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(Sort.by(direction, params.getField())));

        List<Article> articles = pageOfArticles.stream()
                .peek(article -> {
                    if(article.getContent().length() > 255)
                        article.setContent(article.getContent().substring(0, 255) + "...");
                })
                .collect(Collectors.toList());

        List<EntityArticleResponseDto> articleDtos = getListOfArticleDtos(articles);

        return ArticlePageResponseDto.builder()
                .totalElements(pageOfArticles.getTotalElements())
                .totalPages(pageOfArticles.getTotalPages())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .articles(articleDtos)
                .build();
    }


    public ArticlePageResponseDto getArticlesPageByAuthor(Integer userId, PaginationParameters params) {

        params.setField(paramsValidator.getCorrectValue(params.getField()).getHqlField());

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<Article> pageOfArticles = articleRepository.findAllByAuthorId(
                userId,
                PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(Sort.by(direction, params.getField())));

        List<Article> articles = pageOfArticles.stream()
                .peek(article -> {
                    if(article.getContent().length() > 255)
                        article.setContent(article.getContent().substring(0, 255)  + "...");
                })
                .collect(Collectors.toList());

        List<EntityArticleResponseDto> articleDtos = getListOfArticleDtos(articles);

        return ArticlePageResponseDto.builder()
                .totalElements(pageOfArticles.getTotalElements())
                .totalPages(pageOfArticles.getTotalPages())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .articles(articleDtos)
                .build();
    }


    public ArticlePageResponseDto getLikedArticlesPageByUser(Integer userId, PaginationParameters params) {

        Page<Like> pageOfLikes = likeRepository.findAllByUserId(userId,
                PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(Sort.by("id")));

        int[] articleIds = pageOfLikes.stream()
                .mapToInt(Like::getArticleId)
                .toArray();

        List<Article> articles = new ArrayList<>();

        for(Integer articleId : articleIds) {
            Article foundArticle = articleRepository.findById(articleId)
                    .orElseThrow(() -> new IllegalStateException("Cannot find article"));
            if(foundArticle.getContent().length() > 255)
                foundArticle.setContent(foundArticle.getContent().substring(0, 255)  + "...");
            articles.add(foundArticle);
        }

        List<EntityArticleResponseDto> articleDtos = getListOfArticleDtos(articles);

        return ArticlePageResponseDto.builder()
                .totalElements(pageOfLikes.getTotalElements())
                .totalPages(pageOfLikes.getTotalPages())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .articles(articleDtos)
                .build();
    }

    public ArticlePageResponseDto getBookmarkedArticlesPageByUser(Integer userId, PaginationParameters params) {

        Page<Bookmark> pageOfBookmarks = bookmarkRepository.findAllByUserId(userId,
                PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(Sort.by("id")));

        int[] articleIds = pageOfBookmarks.stream()
                .mapToInt(Bookmark::getArticleId)
                .toArray();

        List<Article> articles = new ArrayList<>();

        for(Integer articleId : articleIds) {
            Article foundArticle = articleRepository.findById(articleId)
                    .orElseThrow(() -> new IllegalStateException("Cannot find article"));
            if(foundArticle.getContent().length() > 255)
                foundArticle.setContent(foundArticle.getContent().substring(0, 255)  + "...");
            articles.add(foundArticle);
        }

        List<EntityArticleResponseDto> articleDtos = getListOfArticleDtos(articles);

        return ArticlePageResponseDto.builder()
                .totalElements(pageOfBookmarks.getTotalElements())
                .totalPages(pageOfBookmarks.getTotalPages())
                .page(params.getPage())
                .pageSize(params.getPageSize())
                .articles(articleDtos)
                .build();
    }


    private List<EntityArticleResponseDto> getListOfArticleDtos(List<Article> articles) {

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
