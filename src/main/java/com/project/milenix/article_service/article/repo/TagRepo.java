package com.project.milenix.article_service.article.repo;

import com.project.milenix.article_service.article.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepo extends JpaRepository<Tag, Integer> {
}
