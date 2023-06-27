package com.project.milenix.article_service.article.service;

import com.project.milenix.article_service.article.dto.ArticleRequestDto;
import com.project.milenix.article_service.article.model.Article;
import com.project.milenix.article_service.article.model.Bookmark;
import com.project.milenix.article_service.article.model.Like;
import com.project.milenix.article_service.article.repo.ArticleRepository;
import com.project.milenix.article_service.article.repo.BookmarkRepository;
import com.project.milenix.article_service.article.repo.LikeRepository;
import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.dto.EntityArticleResponseDto;
import com.project.milenix.article_service.exception.ArticleException;
import com.project.milenix.article_service.util.ArticlePaginationParametersValidator;
import com.project.milenix.authentication_service.util.JwtUtil;
import com.project.milenix.category_service.category.service.CategoryService;
import com.project.milenix.user_service.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService extends ArticleCommonService{

  private final ArticleRepository articleRepository;
  private final LikeRepository likeRepository;
  private final BookmarkRepository bookmarkRepository;
  private final ArticlePaginationParametersValidator paramsValidator;
  private final UserService userService;
  private final CategoryService categoryService;

  public EntityArticleResponseDto getArticle(Integer id) throws ArticleException{
    // TODO: handle exception
    Article article = articleRepository.findById(id)
            .orElseThrow(() -> new ArticleException(String.format("Article with id %d cannot be found", id)));

    articleRepository.increaseViewsById(id);

    return new EntityArticleResponseDto(article);
  }

  public List<EntityArticleResponseDto> getAllArticles(){
    List<Article> articles = articleRepository.findAll();

    for (Article article : articles) {
      if (article.getMainImagePath() != null) {
        article.setMainImagePath("article-images/" + article.getId() + "/" + article.getMainImagePath());
      }
    }

    return articles.stream()
            .map(EntityArticleResponseDto::new)
            .collect(Collectors.toList());
  }

  public List<EntityArticleResponseDto> findArticlesWithSorting(String field, String dirVal){

    field = paramsValidator.getCorrectValue(field).getHqlField();

    Sort.Direction direction = (dirVal != null && dirVal.equalsIgnoreCase("desc"))
            ? Sort.Direction.DESC : Sort.Direction.ASC;

    List<Article> articles = articleRepository.findAll(Sort.by(direction, field));

    return getListOfArticleDTOS(articles);
  }

  public Integer saveArticle(ArticleRequestDto articleRequestDto, Integer userId, String fileName) {

    long numberOfWords = articleRequestDto.getContent().split(" ").length;
    int minutesToRead = (int) Math.ceil(numberOfWords / 200.0);

    try {
      Article article = Article.builder()
              .title(articleRequestDto.getTitle())
              .content(articleRequestDto.getContent())
              .mainImagePath(fileName)
              .minutesToRead(minutesToRead)
              .publishingDate(LocalDateTime.now())
              .numberOfLikes(0)
              .numberOfViews(0)
              .author(userService.getUser(userId))
              .category(categoryService.getCategory(articleRequestDto.getCategoryId()))
              .build();
      Article savedArticle = articleRepository.save(article);
      return savedArticle.getId();
    } catch (Exception e){
      throw new RuntimeException("Cannot save article");
    }
  }

  public EntityArticleResponseDto updateArticle(Integer id, ArticleRequestDto articleRequestDto, MultipartFile mainImage) throws ArticleException{
    // TODO: handle exception

    Article foundArticleInDb = articleRepository.findById(id)
        .orElseThrow(() -> new ArticleException(String.format("Article with id %d cannot be found", id)));

    if(articleRequestDto == null) // TODO: how can this happen?
      return new EntityArticleResponseDto(foundArticleInDb);

    if(articleRequestDto.getTitle() != null)
      foundArticleInDb.setTitle(articleRequestDto.getTitle());
    if(articleRequestDto.getContent() != null)
      foundArticleInDb.setContent(articleRequestDto.getContent());
    if(mainImage != null)
      foundArticleInDb.setMainImagePath(mainImage.getOriginalFilename());


    try {
      if (articleRequestDto.getCategoryId() != null)
        foundArticleInDb.setCategory(categoryService.getCategory(articleRequestDto.getCategoryId()));
    } catch (Exception e){
      throw new RuntimeException("Cannot save category");
    }

    Article updatedArticle = articleRepository.save(foundArticleInDb); // TODO: do we really need this?

    return new EntityArticleResponseDto(updatedArticle);
  }

  public void deleteArticle(Integer id) throws ArticleException{
    // TODO: handle exception

    Article foundArticleInDb = articleRepository.findById(id)
        .orElseThrow(() -> new ArticleException(String.format("Article with id %d cannot be found", id)));

    articleRepository.delete(foundArticleInDb);
  }

  public void likeArticle(Integer articleId, HttpServletRequest httpServletRequest) {
    Integer userId = JwtUtil.getIdFromToken(httpServletRequest);
    likeRepository.save(Like.builder().articleId(articleId).userId(userId).build());
    articleRepository.increaseLikesById(articleId);
  }

  public void bookmarkArticle(Integer articleId, HttpServletRequest httpServletRequest) {
    Integer userId = JwtUtil.getIdFromToken(httpServletRequest);
    bookmarkRepository.save(Bookmark.builder().articleId(articleId).userId(userId).build());
  }

  public void deleteLikeArticle(Integer articleId, HttpServletRequest httpServletRequest) {
    Integer userId = JwtUtil.getIdFromToken(httpServletRequest);
    likeRepository.deleteByArticleIdAndUserId(userId, articleId);
    articleRepository.decreaseLikesById(articleId);
  }

  public void deleteBookmarkArticle(Integer articleId, HttpServletRequest httpServletRequest) {
    Integer userId = JwtUtil.getIdFromToken(httpServletRequest);
    bookmarkRepository.deleteByArticleIdAndUserId(userId, articleId);
  }

  public List<EntityArticleResponseDto> findHotArticles(PaginationParameters params, Integer size) {

    Page<Article> pageOfArticles = articleRepository.searchHotArticles("number_of_views", size,
            PageRequest.of(0, Integer.MAX_VALUE).withSort(Sort.by(Sort.Direction.DESC, "number_of_views")));

    List<Article> articles = pageOfArticles.stream()
            .peek(article -> {
              if(article.getContent().length() > 255)
                article.setContent(article.getContent().substring(0, 255)  + "...");
            })
            .collect(Collectors.toList());

    return getListOfArticleDTOS(articles);
  }
}
