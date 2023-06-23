package com.project.milenix.article_service.article.controller;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.ArticleRequestDto;
import com.project.milenix.article_service.article.service.ArticleService;
import com.project.milenix.article_service.article.service.TagService;
import com.project.milenix.article_service.exception.ArticleException;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;
import com.project.milenix.file.service.ArticleFileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;
  private final ArticleFileStorageService fileStorageService;
  private final TagService tagService;

  @GetMapping
  @PreAuthorize("hasAuthority('article:read:all')")
  @ResponseStatus(HttpStatus.OK)
  public List<EntityArticleResponseDto> getAllArticles(){
    return articleService.getAllArticles();
  }

  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public EntityArticleResponseDto getArticleById(@PathVariable("id") Integer id) throws ArticleException {
    // TODO: handle exception
    return articleService.getArticle(id);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('article:add')")
  @ResponseStatus(HttpStatus.CREATED)
  public Integer saveArticle(@Valid ArticleRequestDto articleRequestDto,
                             @RequestParam("mainImage")MultipartFile mainImage,
                             @RequestParam(value = "images", required = false)MultipartFile[] images,
                             @RequestParam(value = "tags", required = false) String[] tags){

    Integer articleId = articleService.saveArticle(articleRequestDto, mainImage.getOriginalFilename());
    String fileName = fileStorageService.storeFile(articleId, mainImage);

    if(images != null) {
      Arrays.stream(images)
              .forEach(image -> fileStorageService.storeFile(articleId, image));
    }

    if(tags != null){
      tagService.saveTags(articleId, tags);
    }

    return articleId;
  }

  @PutMapping("{id}")
  @PreAuthorize("hasAuthority('article:update')") // TODO: check use in SecurityConfig
  @ResponseStatus(HttpStatus.OK)
  public EntityArticleResponseDto updateArticle(@PathVariable("id") Integer id, @RequestParam(required = false) ArticleRequestDto articleRequestDto,
                                                @RequestParam(value = "mainImage", required = false)MultipartFile file,
                                                @RequestParam(value = "images", required = false)MultipartFile[] images,
                                                @RequestParam(value = "tags", required = false) String[] tags)
      throws ArticleException {
    // TODO: handle exception

    EntityArticleResponseDto entityArticleResponseDto = articleService.updateArticle(id, articleRequestDto, file);

    if(file != null){
      String fileName = fileStorageService.storeFile(id, file);
    }

    if(images != null) {
      Arrays.stream(images)
              .forEach(image -> fileStorageService.storeFile(id, image));
    }

    if(tags != null){
      tagService.saveTags(id, tags);
    }

    return entityArticleResponseDto;
  }

  @DeleteMapping("{id}")
  @PreAuthorize("hasAuthority('article:delete')") // TODO: check use in SecurityConfig
  @ResponseStatus(HttpStatus.OK)
  public void deleteArticle(@PathVariable("id") Integer id) throws ArticleException {
    // TODO: handle exception

    articleService.deleteArticle(id);
  }

  @GetMapping(params = {"field"})
  @PreAuthorize("hasAuthority('article:read:all')")
  @ResponseStatus(HttpStatus.OK)
  public List<EntityArticleResponseDto> getArticlesWithSort(
                                    @RequestParam(value = "field") String field,
                                    @RequestParam(value = "direction", required = false) String direction){
    return articleService.findArticlesWithSorting(field, direction);
  }

  @PostMapping("{articleId}/like")
  @PreAuthorize("hasAuthority('article:like')")
  @ResponseStatus(HttpStatus.OK)
  public void likeArticle(@PathVariable("articleId") Integer articleId,
                          @RequestParam("userId") Integer userId){
    articleService.likeArticle(articleId, userId);
  }

  @PostMapping("{articleId}/bookmark")
  @PreAuthorize("hasAuthority('article:bookmark')")
  @ResponseStatus(HttpStatus.OK)
  public void bookmarkArticle(@PathVariable("articleId") Integer articleId,
                          @RequestParam("userId") Integer userId){
    articleService.bookmarkArticle(articleId, userId);
  }

  @DeleteMapping("{articleId}/like")
  @PreAuthorize("hasAuthority('article:like')")
  @ResponseStatus(HttpStatus.OK)
  public void deleteLikeArticle(@PathVariable("articleId") Integer articleId,
                          @RequestParam("userId") Integer userId){
    articleService.deleteLikeArticle(articleId, userId);
  }

  @DeleteMapping("{articleId}/bookmark")
  @PreAuthorize("hasAuthority('article:bookmark')")
  @ResponseStatus(HttpStatus.OK)
  public void deleteBookmarkArticle(@PathVariable("articleId") Integer articleId,
                              @RequestParam("userId") Integer userId) {
    articleService.deleteBookmarkArticle(articleId, userId);
  }

  @GetMapping("hot")
  @ResponseStatus(HttpStatus.OK)
  public List<EntityArticleResponseDto> getHotArticlesWithPagination(PaginationParameters params,
                                                                     @RequestParam("size") Integer size){
    return articleService.findHotArticles(params, size);
  }
}
