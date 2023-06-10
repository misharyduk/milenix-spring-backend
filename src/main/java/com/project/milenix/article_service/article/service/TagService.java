package com.project.milenix.article_service.article.service;

import com.project.milenix.article_service.article.model.Tag;
import com.project.milenix.article_service.article.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepo tagRepo;

    public void saveTags(Integer articleId, String[] tags) {
        for (String tag : tags) {
            tagRepo.save(Tag.builder().articleId(articleId).tag(tag).build());
        }
    }
}
