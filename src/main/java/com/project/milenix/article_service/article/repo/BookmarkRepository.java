package com.project.milenix.article_service.article.repo;

import com.project.milenix.article_service.article.model.Bookmark;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Page<Bookmark> findAllByUserId(Integer userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM article_user_bookmark b WHERE b.articleId=?1 AND b.userId=?2")
    void deleteByArticleIdAndUserId(Integer userId, Integer articleId);
}
