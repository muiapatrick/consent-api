package com.techminia.collection.api.validation.clienttype;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Documented
@Constraint(validatedBy = ValidClientTypeValidator.class)
@Target({ANNOTATION_TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidClientType {
    String message() default "Invalid Client Type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
