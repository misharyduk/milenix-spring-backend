package com.project.milenix.article_service.util;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

@Component
public class ArticlePaginationParametersValidator{

    public enum ArticleFields {

        ID(Lists.newArrayList("id", "ID"), "id"),
        TITLE(Lists.newArrayList("title", "TITLE", "name"), "title"),
        PUBLISHING_DATE(Lists.newArrayList("publishing_date", "date", "publishingDate",
                "PUBLISHING_DATE", "PUBLISHINGDATE", "DATE"), "publishingDate"),
        NUMBER_OF_VIEWS(Lists.newArrayList("number_of_views", "number_of_view", "views", "view", "numberOfViews",
                "NUMBER_OF_VIEWS", "NUMBER_OF_VIEW", "NUMBEROFVIEWS", "VIEWS", "VIEWS"), "numberOfViews"),
        NUMBER_OF_LIKES(Lists.newArrayList("number_of_likes", "number_of_like", "likes", "like", "numberOfLikes",
                "NUMBER_OF_LIKES", "NUMBER_OF_LIKE", "NUMBEROFLIKES", "LIKES", "LIKE"), "numberOfLikes");

        private final List<String> fieldValues;
        private final String hqlField;

        ArticleFields(List<String> fieldValues, String hqlField) {
            this.fieldValues = fieldValues;
            this.hqlField = hqlField;
        }

        public List<String> getFieldValues() {
            return fieldValues;
        }

        public String getHqlField() {
            return hqlField;
        }
    }

    public ArticleFields getCorrectValue(String field) {
        EnumSet<ArticleFields> articleFieldsSet = EnumSet.allOf(ArticleFields.class);
        for (ArticleFields articleFields : articleFieldsSet) {
            List<String> fieldValues = articleFields.getFieldValues();
            for (String value : fieldValues) {
                if (value.equals(field.toUpperCase())) {
                    return articleFields;
                }
            }
        }
        return ArticleFields.ID;
    }
}
