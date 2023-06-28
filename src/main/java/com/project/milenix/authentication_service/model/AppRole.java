package com.project.milenix.authentication_service.model;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.project.milenix.authentication_service.model.AppAuthority.*;

public enum AppRole {
    USER(Sets.newHashSet(
            ARTICLE_READ, ARTICLE_ADD, /*ARTICLE_UPDATE, ARTICLE_DELETE,*/
            USER_READ,
//            USER_UPDATE, USER_DELETE,
            CATEGORY_READ, CATEGORY_READ_ALL,
            ARTICLE_LIKE, ARTICLE_BOOKMARK
    )),
    MODERATOR(Sets.newHashSet(
            ARTICLE_READ, ARTICLE_READ_ALL, ARTICLE_ADD, ARTICLE_UPDATE, ARTICLE_DELETE,
            USER_READ, USER_READ_ALL, USER_UPDATE, USER_DELETE,
            CATEGORY_READ, CATEGORY_READ_ALL, CATEGORY_ADD, CATEGORY_UPDATE, CATEGORY_DELETE,
            ARTICLE_LIKE, ARTICLE_BOOKMARK
    )),
    ADMIN(Sets.newHashSet(
            ARTICLE_READ, ARTICLE_READ_ALL, ARTICLE_ADD, ARTICLE_UPDATE, ARTICLE_DELETE,
            USER_READ, USER_READ_ALL, USER_ADD, USER_UPDATE, USER_DELETE,
            CATEGORY_READ, CATEGORY_READ_ALL, CATEGORY_ADD, CATEGORY_UPDATE, CATEGORY_DELETE,
            ARTICLE_LIKE, ARTICLE_BOOKMARK
    ));

    private final Set<AppAuthority> authorities;

    AppRole(Set<AppAuthority> authorities){
        this.authorities = authorities;
    }

    public Set<AppAuthority> getAuthorities() {
        return authorities;
    }

    public Collection<? extends GrantedAuthority> getGranterAuthorities(){
        Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getPermission()))
                .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
