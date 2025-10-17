package com.yeeiee.domain.validate;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Set;

/**
 * <p>
 * 校验表单对象内两个字段值是否相等
 * </p>
 *
 * @author chen
 * @since 2025-10-13
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = HttpMethod.HttpMethodValidator.class)
public @interface HttpMethod {
    String message() default "仅适用 GET|POST|PUT|DELETE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class HttpMethodValidator implements ConstraintValidator<HttpMethod, String> {
        private static final Set<String> METHOD_SET = Set.of("GET", "POST", "PUT", "DELETE");

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            return METHOD_SET.contains(value.toUpperCase());
        }
    }
}