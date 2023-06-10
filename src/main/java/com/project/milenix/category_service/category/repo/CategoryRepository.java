package com.project.milenix.category_service.category.repo;

import com.project.milenix.category_service.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c WHERE " +
            "c.name iLIKE CONCAT('%', :query, '%')")
    List<Category> searchCategories(@Param("query") String query, Sort sort);

    @Query(value = "SELECT * FROM category c WHERE " +
                "c.name iLIKE CONCAT('%', :query, '%')",
            countQuery = "SELECT count(*) FROM category c WHERE " +
                    "c.name iLIKE CONCAT('%', :query, '%')",
            nativeQuery = true)
    Page<Category> searchCategoriesWithPagination(@Param("query") String query,
                                                  Pageable pageable);

}