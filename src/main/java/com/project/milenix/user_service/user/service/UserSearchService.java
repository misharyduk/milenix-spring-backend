package com.project.milenix.user_service.user.service;

import com.project.milenix.PaginationParameters;
import com.project.milenix.article_service.article.service.ArticleDevService;
import com.project.milenix.user_service.user.dto.EntityUserResponseDto;
import com.project.milenix.user_service.user.dto.UserPageResponseDto;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.user_service.user.repo.UserRepository;
import com.project.milenix.user_service.util.UserPaginationParametersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSearchService extends UserServiceCommon{

    private final UserRepository userRepository;
    private final ArticleDevService articleDevService;
    private final UserPaginationParametersValidator paramsValidator;

    public List<EntityUserResponseDto> searchUsers(String value, String fieldVal, String dirVal){

        if(value.isBlank()){
            return new ArrayList<>();
        }

        Sort.Direction direction = (dirVal != null && dirVal.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        String field = fieldVal == null ? "id" : fieldVal;
        field = paramsValidator.getCorrectValue(field).getHqlField();

        return userRepository.searchUsers(value, Sort.by(direction, field)).stream()
                .peek(user -> user.setPage(articleDevService.getArticlesPageByAuthor(
                        user.getId(),
                        PaginationParameters.builder()
                                .page(1).pageSize(10).field("numberOfViews").direction("asc").build())))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserPageResponseDto searchUserWithPagination(String value, PaginationParameters params){

        if(value.isBlank()){
            return new UserPageResponseDto();
        }

        params.setField(paramsValidator.getCorrectValue(params.getField()).name());

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<User> usersPage = userRepository.searchUserWithPagination(
                value, PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(direction, params.getField()));

        return mapToPageDTO(params.getPage(), params.getPageSize(), usersPage);
    }

}
