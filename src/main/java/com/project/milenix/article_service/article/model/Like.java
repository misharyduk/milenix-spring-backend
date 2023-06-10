package com.project.milenix.article_service.article.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "article_user_like")
public class Like {
    @SequenceGenerator(
            name = "like_sequence",
            sequenceName = "like_sequence"
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "like_sequence"
    )
    private Integer id;
    private Integer articleId;
    private Integer userId;
}
