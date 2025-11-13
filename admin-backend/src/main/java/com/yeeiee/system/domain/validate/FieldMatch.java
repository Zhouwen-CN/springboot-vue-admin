package com.yeeiee.system.domain.validate;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.SneakyThrows;
import lombok.val;

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
 * @since 2025-05-27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatch.FieldMatchValidator.class)
public @interface FieldMatch {
    String field();

    String confirmField();

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

        private String field;
        private String confirmField;

        @Override
        public void initialize(FieldMatch constraintAnnotation) {
            field = constraintAnnotation.field();
            confirmField = constraintAnnotation.confirmField();
        }

        @SneakyThrows
        @Override
        public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
            // 通过反射获取两个字段的值
            val clazz = o.getClass();
            val firstField = clazz.getDeclaredField(field);
            firstField.setAccessible(true);
            val firstFieldValue = firstField.get(o);

            val secondField = clazz.getDeclaredField(confirmField);
            secondField.setAccessible(true);
            val secondFieldValue = secondField.get(o);

            // 比较两个字段的值是否相等
            return firstFieldValue != null && firstFieldValue.equals(secondFieldValue);
        }
    }
}
