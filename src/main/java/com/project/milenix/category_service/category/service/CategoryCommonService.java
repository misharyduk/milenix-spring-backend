package com.project.milenix.category_service.category.service;

import com.project.milenix.category_service.category.dto.EntityCategoryResponseDto;
import com.project.milenix.category_service.category.model.Category;
import com.project.milenix.PaginationParameters;

import java.util.HashMap;
import java.util.Map;

public abstract class CategoryCommonService {

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

    protected EntityCategoryResponseDto mapToDto(Category category) {
        return EntityCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .page(category.getPage())
                .build();
    }
}
