package com.project.milenix.category_service.util;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

@Component
public class CategoryPaginationParametersValidator  {

    enum CategoryFields{

        ID(Lists.newArrayList("id", "ID")),
        NAME(Lists.newArrayList("name", "NAME", "title", "TITLE"));

        private final List<String> fieldValues;


        CategoryFields(List<String> fieldValues) {
            this.fieldValues = fieldValues;
        }

        public List<String> getFieldValues() {
            return fieldValues;
        }
    }

    public String getCorrectValue(String field) {
        EnumSet<CategoryFields> categoryFieldsSet = EnumSet.allOf(CategoryFields.class);
        for (CategoryFields categoryFields : categoryFieldsSet) {
            List<String> fieldValues = categoryFields.getFieldValues();
            for (String value : fieldValues) {
                if (value.equals(field.toUpperCase())) {
                    return categoryFields.name().toLowerCase();
                }
            }
        }
        return "id";
    }
}
