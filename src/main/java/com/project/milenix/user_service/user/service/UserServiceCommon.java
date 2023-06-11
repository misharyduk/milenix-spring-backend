package com.project.milenix.user_service.user.service;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.service.ArticleDevService;
import com.project.milenix.user_service.user.dto.EntityUserResponseDto;
import com.project.milenix.user_service.user.dto.UserPageResponseDto;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.file.util.UserFileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class UserServiceCommon {

    @Autowired
    private ArticleDevService articleDevService;
    @Autowired
    private UserFileStorageProperties properties;

    protected Map<String, String> toMap(PaginationParameters params){
        return new HashMap<>(){
            {
                this.put("page", String.valueOf(params.getPage()));
                this.put("pageSize", String.valueOf(params.getPageSize()));
                this.put("field", params.getField());
                this.put("direction", params.getDirection());
            }
        };
    }

    protected EntityUserResponseDto mapToDto(User user) {
        String imagePath = properties.getUploadDir() + File.separator + user.getId() + File.separator + user.getImage();
        return EntityUserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .image(imagePath)
                .role(user.getRole())
                .page(user.getPage())
                .build();
    }

    protected UserPageResponseDto mapToPageDTO(Integer offset, Integer pageSize, Page<User> usersPage) {
        List<EntityUserResponseDto> users = usersPage.stream()
                .peek(user -> user.setPage(articleDevService.getArticlesPageByAuthor(
                        user.getId(),
                        PaginationParameters.builder()
                                .page(1).pageSize(10).field("numberOfViews").direction("asc").build()
                )))
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return UserPageResponseDto.builder()
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .page(offset)
                .pageSize(pageSize)
                .users(users)
                .build();
    }

}
