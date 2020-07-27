package com.techminia.collection.api.validation.phonenumber;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidatePhone.class)
@Target({ANNOTATION_TYPE, FIELD, METHOD})
@Retention(RUNTIME)
public @interface ValidPhone {
    String message() default "Invalid Phone number 2547XXXXXXXX";
    Class<?>[] groups() default  {};
    Class<? extends Payload>[] payload() default {};

}



