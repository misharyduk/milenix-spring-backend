package com.project.milenix.user_service.user.repo;

import com.project.milenix.user_service.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByEmail(String email);

  @Query(value = "SELECT u FROM app_user u WHERE u.firstName " +
                 "iLIKE CONCAT('%', :value, '%') " +
                 "OR u.lastName iLIKE CONCAT('%', :value, '%')")
  List<User> searchUsers(@Param("value") String value, Sort sort);

  @Query(value = "SELECT * FROM app_user u WHERE u.first_name " +
                  "iLIKE CONCAT('%', :value, '%') " +
                  "OR u.last_name iLIKE CONCAT('%', :value, '%')",
          countQuery = "SELECT count(*) FROM app_user u WHERE u.first_name " +
                  "iLIKE CONCAT('%', :value, '%') " +
                  "OR u.last_name iLIKE CONCAT('%', :value, '%')",
          nativeQuery = true)
  Page<User> searchUserWithPagination(@Param("value") String value, Pageable pageable);
}
