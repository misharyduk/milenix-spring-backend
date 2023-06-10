package com.project.milenix.article_service.article.repo;

import com.project.milenix.article_service.article.model.Like;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Page<Like> findAllByUserId(Integer userId, Pageable pageable);

    long countByArticleId(Integer articleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM article_user_like l WHERE l.articleId=?1 AND l.userId=?2")
    void deleteByArticleIdAndUserId(Integer userId, Integer articleId);
}
