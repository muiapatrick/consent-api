package com.techminia.collection.api.validation.clienttype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class ValidClientTypeValidator implements ConstraintValidator<ValidClientType, String> {
    @Override
    public void initialize(ValidClientType constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValidGrantType(value);
    }

    public static boolean isValidGrantType(String value){
        if (value == null || value.trim().isEmpty())
            return true;
        if (value.trim().equals("1") || value.trim().equals("2"))
            return true;

        return false;
    }

}
