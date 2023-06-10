package com.project.milenix.user_service.user.service;

import com.project.milenix.PaginationParameters;
import com.project.milenix.user_service.user.dto.UserPageResponseDto;
import com.project.milenix.user_service.user.model.User;
import com.project.milenix.user_service.user.repo.UserRepository;
import com.project.milenix.user_service.util.UserPaginationParametersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPaginationService extends UserServiceCommon{

    private final UserRepository userRepository;
    private final UserPaginationParametersValidator paramsValidator;

    public UserPageResponseDto findUserWithPaginationAndSort(PaginationParameters params) {

        params.setField(paramsValidator.getCorrectValue(params.getField()).getHqlField());

        Sort.Direction direction = Sort.Direction.valueOf(params.getDirection());

        Page<User> usersPage = userRepository.findAll(
                PageRequest.of(params.getPage() - 1, params.getPageSize()).withSort(direction, params.getField()));

        return mapToPageDTO(params.getPage(), params.getPageSize(), usersPage);
    }

}
