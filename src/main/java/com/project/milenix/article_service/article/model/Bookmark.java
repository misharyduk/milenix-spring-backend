package com.project.milenix.article_service.article.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "article_user_bookmark")
public class Bookmark {
    @SequenceGenerator(
            name = "bookmark_sequence",
            sequenceName = "bookmark_sequence"
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bookmark_sequence"
    )
    private Integer id;
    private Integer articleId;
    private Integer userId;
}
