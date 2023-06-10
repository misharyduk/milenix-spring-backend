package com.project.milenix.user_service.user.model;

import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "app_user")
public class User {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence"
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private String image;
    @Transient
//    private List<EntityArticleResponseDto> articles = new ArrayList<>();
    private ArticlePageResponseDto page;

}
