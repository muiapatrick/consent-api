package com.techminia.collection.api.validation.email;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(value={ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
@Constraint(validatedBy={EmailValidator.class})
@Documented
public @interface ValidEmail {
    public String message() default "Must be valid email address";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

