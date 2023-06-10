package com.project.milenix.article_service.article.model;

import com.project.milenix.category_service.category.dto.CategoryResponseDto;
import com.project.milenix.user_service.user.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Article {
    @SequenceGenerator(
            name = "article_sequence",
            sequenceName = "article_sequence"
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "article_sequence"
    )
    private Integer id;
    @Column(length = 500)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column
    private String mainImagePath;
    @Column
    private LocalDateTime publishingDate;
    @Column
    private Integer minutesToRead;
    @Column
    @JsonIgnore
    private Integer authorId;
    @Column
    @JsonIgnore
    private Integer categoryId;
    @Column(columnDefinition = "integer not null default 0")
    private Integer numberOfViews = 0;
    @Column(columnDefinition = "integer not null default 0")
    private Integer numberOfLikes = 0;
    @Transient
    private UserResponseDto author;
    @Transient
    private CategoryResponseDto category;
    @Transient
    private String imagesDir;

    public String getImagesDir(){
        return "images/article-images/" + getId();
    }
}
