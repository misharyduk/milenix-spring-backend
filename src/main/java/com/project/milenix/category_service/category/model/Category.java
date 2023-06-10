package com.project.milenix.category_service.category.model;

import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence"
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    private Integer id;
    @Column(unique = true)
    private String name;
    @Transient
    private ArticlePageResponseDto page;
}
