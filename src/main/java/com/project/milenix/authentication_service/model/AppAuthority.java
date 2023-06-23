package com.project.milenix.authentication_service.model;

public enum AppAuthority {
    ARTICLE_READ("article:read"),
    ARTICLE_READ_ALL("article:read:all"),
    ARTICLE_ADD("article:add"),
    ARTICLE_UPDATE("article:update"),
    ARTICLE_DELETE("article:delete"),

    USER_READ("user:read"),
    USER_READ_ALL("user:read:all"),
    USER_ADD("user:add"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),

    CATEGORY_READ("category:read"),
    CATEGORY_READ_ALL("category:read:all"),
    CATEGORY_ADD("category:add"),
    CATEGORY_UPDATE("category:update"),
    CATEGORY_DELETE("category:delete"),

    ARTICLE_LIKE("article:like"),
    ARTICLE_BOOKMARK("article:bookmark");

    private final String permission;

    AppAuthority(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
