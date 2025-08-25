package com.yeeiee.meter;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.MeterTag;
import io.micrometer.core.aop.MeterTagAnnotationHandler;
import jakarta.annotation.Nullable;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * <pre>
 * 动态维度标签的支持 {@link MeterTag}
 * 但是目前只支持 {@link Timed}，不支持 {@link Counted}
 * Gauge 目前注解都不支持
 * </pre>
 *
 * @author chen
 * @since 2025-08-14
 */

@Configuration
public class MicrometerDynamicTagConfig {
    private final ExpressionParser PARSER = new SpelExpressionParser();
    @Bean
    public MeterTagAnnotationHandler meterTagAnnotationHandler() {
        return new MeterTagAnnotationHandler(
                resolverProvider -> {
                    throw new UnsupportedOperationException("Unsupported value resolver, Please use expression resolver");
                },
                ValueExpressionResolver -> (String expression, @Nullable Object parameter) -> {
                    val context = new StandardEvaluationContext(parameter);
                    return PARSER.parseExpression(expression).getValue(context, String.class);
                }
        );
    }

}
