package com.project.milenix.user_service.user.dto;

import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.user_service.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityUserResponseDto { // MAIN RESPONSE DTO FOR INDIVIDUAL USER
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String image;
    private ArticlePageResponseDto page;

    public EntityUserResponseDto(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.image = user.getImage();
    }
}
