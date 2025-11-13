package com.yeeiee.scheduler.domain.validate;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Constraint(validatedBy = CronExpression.CronExpressionValidator.class)
public @interface CronExpression {
    String message() default "Cron表达式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CronExpressionValidator implements ConstraintValidator<CronExpression, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            return org.quartz.CronExpression.isValidExpression(value);
        }
    }
}