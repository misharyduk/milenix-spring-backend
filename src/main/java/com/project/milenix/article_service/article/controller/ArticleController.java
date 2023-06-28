package com.project.milenix.article_service.article.controller;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.article_service.article.dto.ArticleRequestDto;
import com.project.milenix.article_service.article.service.ArticleService;
import com.project.milenix.article_service.article.service.TagService;
import com.project.milenix.article_service.exception.ArticleException;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;
import com.project.milenix.authentication_service.service.AuthenticationService;
import com.project.milenix.authentication_service.util.JwtUtil;
import com.project.milenix.category_service.category.dto.EntityCategoryResponseDto;
import com.project.milenix.file.service.ArticleFileStorageService;
import com.project.milenix.user_service.exception.CustomUserException;
import jakarta.servlet.http.HttpServletRequest;
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
  private final AuthenticationService authenticationService;

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
                             @RequestParam(value = "tags", required = false) String[] tags,
                             HttpServletRequest httpServletRequest){

    Integer userId = JwtUtil.getIdFromToken(httpServletRequest);
    Integer articleId = articleService.saveArticle(articleRequestDto, userId, mainImage.getOriginalFilename());
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
  @PreAuthorize("@authenticationService.hasAccessToArticleById(#id, #httpServletRequest) || hasAuthority('article:update')")
  @ResponseStatus(HttpStatus.OK)
  public EntityArticleResponseDto updateArticle(@PathVariable("id") Integer id, @RequestParam(required = false) ArticleRequestDto articleRequestDto,
                                                @RequestParam(value = "mainImage", required = false)MultipartFile file,
                                                @RequestParam(value = "images", required = false)MultipartFile[] images,
                                                @RequestParam(value = "tags", required = false) String[] tags,
                                                HttpServletRequest httpServletRequest)
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
  @PreAuthorize("@authenticationService.hasAccessToArticleById(#id, #httpServletRequest) || hasAuthority('article:delete')")
  @ResponseStatus(HttpStatus.OK)
  public void deleteArticle(@PathVariable("id") Integer id, HttpServletRequest httpServletRequest) throws ArticleException {
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
//                          @RequestParam("userId") Integer userId, // TODO fetch user from JWT token not from request
                          HttpServletRequest httpServletRequest){
    articleService.likeArticle(articleId, httpServletRequest);
  }

  @PostMapping("{articleId}/bookmark")
  @PreAuthorize("hasAuthority('article:bookmark')")
  @ResponseStatus(HttpStatus.OK)
  public void bookmarkArticle(@PathVariable("articleId") Integer articleId,
//                              @RequestParam("userId") Integer userId, // TODO fetch user from JWT token not from request
                              HttpServletRequest httpServletRequest){
    articleService.bookmarkArticle(articleId, httpServletRequest);
  }

  @DeleteMapping("{articleId}/like")
  @PreAuthorize("hasAuthority('article:like')")
  @ResponseStatus(HttpStatus.OK)
  public void deleteLikeArticle(@PathVariable("articleId") Integer articleId,
//                                @RequestParam("userId") Integer userId, // TODO fetch user from JWT token not from request
                                HttpServletRequest httpServletRequest){ // TODO get user from JWT token
    articleService.deleteLikeArticle(articleId, httpServletRequest);
  }

  @DeleteMapping("{articleId}/bookmark")
  @PreAuthorize("hasAuthority('article:bookmark')")
  @ResponseStatus(HttpStatus.OK)
  public void deleteBookmarkArticle(@PathVariable("articleId") Integer articleId,
//                                    @RequestParam("userId") Integer userId, // TODO get user from JWT token
                                    HttpServletRequest httpServletRequest) {
    articleService.deleteBookmarkArticle(articleId, httpServletRequest);
  }

  @GetMapping("hot")
  @ResponseStatus(HttpStatus.OK)
  public ArticlePageResponseDto getHotArticlesWithPagination(PaginationParameters params,
                                                                     @RequestParam("size") Integer size){ // TODO do we really need another size parameter?
    return articleService.findHotArticles(params, size);
  }

  // Personalization
  @GetMapping("interest")
  @ResponseStatus(HttpStatus.OK) //TODO secure this
  public ArticlePageResponseDto getMixArticlesByCategoriesOfUserInterest(PaginationParameters params,
                                                                  HttpServletRequest request) throws CustomUserException {
    Integer userId = JwtUtil.getIdFromToken(request);
    return articleService.getMixArticlesByCategoriesOfUserInterest(userId, params);
  }

  @GetMapping("interest/list")
  @ResponseStatus(HttpStatus.OK) //TODO secure this
  public List<EntityCategoryResponseDto> getArticlesByCategoriesOfUserInterestInList(PaginationParameters params,
                                                                                     HttpServletRequest request) throws CustomUserException {
    Integer userId = JwtUtil.getIdFromToken(request);
    return articleService.getArticlesByCategoriesOfUserInterestInList(userId, params);
  }
}
