package com.project.milenix.authentication_service.service;

import com.project.milenix.article_service.article.model.Article;
import com.project.milenix.article_service.article.repo.ArticleRepository;
import com.project.milenix.article_service.exception.ArticleException;
import com.project.milenix.authentication_service.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final ArticleRepository articleRepository;

    public boolean hasAccessById(Integer userId, HttpServletRequest request){
        Integer subjectId = JwtUtil.getIdFromToken(request);
        return subjectId.equals(userId);
    }

    public boolean hasAccessByUsername(String username, HttpServletRequest request){
        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = tokenHeader.replace("Bearer ", "");
        String subjectUsername = JwtUtil.getUsernameFromToken(request);
        return subjectUsername.equals(username);
    }

    public boolean hasAccessByEmail(String email, HttpServletRequest request){
        String tokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = tokenHeader.replace("Bearer ", "");
        String subjectEmail = JwtUtil.getEmailFromToken(request);
        return subjectEmail.equals(email);
    }

    public boolean hasAccessToArticleById(Integer articleId, HttpServletRequest request) throws ArticleException {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException("Cannot find article"));
        return hasAccessById(article.getAuthor().getId(), request);
    }
}
