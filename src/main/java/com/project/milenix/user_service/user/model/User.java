package com.project.milenix.user_service.user.model;

import com.project.milenix.article_service.article.dto.ArticlePageResponseDto;
import com.project.milenix.authentication_service.model.AppRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "app_user")
public class User implements UserDetails {

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
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private String image;
//    private List<EntityArticleResponseDto> articles = new ArrayList<>();
    @Transient
    private ArticlePageResponseDto page;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AppRole.valueOf(role.toUpperCase()).getGranterAuthorities();
    }
}
