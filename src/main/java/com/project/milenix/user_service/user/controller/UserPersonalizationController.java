package com.project.milenix.user_service.user.controller;

import com.project.milenix.authentication_service.util.JwtUtil;
import com.project.milenix.category_service.category.dto.CategoryRequestDto;
import com.project.milenix.category_service.category.dto.EntityCategoryResponseDto;
import com.project.milenix.user_service.user.service.UserPersonalizationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/personalization") //TODO secure this
public class UserPersonalizationController {

    private final UserPersonalizationService personalizationService;

    @GetMapping("categories")
    public ResponseEntity<?> getCategories(@RequestBody List<CategoryRequestDto> categories,
                                           HttpServletRequest request){
        Integer userId = JwtUtil.getIdFromToken(request);
        personalizationService.saveUserCategories(userId, categories);
        return ResponseEntity.ok().build();
    }
}
