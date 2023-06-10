package com.project.milenix.user_service.util;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;

@Component
public class UserPaginationParametersValidator {

    public enum UserFields {
        ID(Lists.newArrayList("id", "ID"), "id"),
        FIRST_NAME(Lists.newArrayList("first_name", "FIRST_NAME", "firstname", "FIRSTNAME",
                "NAME"), "firstName"),
        LAST_NAME(Lists.newArrayList("last_name", "LAST_NAME", "lastname", "LASTNAME"), "lastName"),

        EMAIL(Lists.newArrayList("email", "EMAIL", "e-mail", "E-MAIL", "main", "MAIN"), "email");

        private final List<String> fieldValues;
        private final String hqlField;


        UserFields(List<String> fieldValues, String hqlField) {
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

    public UserFields getCorrectValue(String field) {
        EnumSet<UserFields> userFieldsSet = EnumSet.allOf(UserFields.class);
        for (UserFields userFields : userFieldsSet) {
            List<String> fieldValues = userFields.getFieldValues();
            for (String value : fieldValues) {
                if (value.equals(field.toUpperCase())) {
                    return userFields;
                }
            }
        }
        return UserFields.ID;
    }
}
