package com.yeeiee.common.meter;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.*;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAspectsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * <pre>
 * 动态维度标签的支持 {@link MeterTag}
 * 只支持 {@link Timed}、{@link Counted}
 * 参考：{@link MetricsAspectsAutoConfiguration}
 * </pre>
 *
 * @author chen
 * @since 2025-08-14
 */

@Configuration
public class MicrometerDynamicTagConfiguration {
    private final ExpressionParser PARSER = new SpelExpressionParser();

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        TimedAspect timedAspect = new TimedAspect(registry);
        timedAspect.setMeterTagAnnotationHandler(new MeterTagAnnotationHandler(resolverProvider -> {
            throw new UnsupportedOperationException("Unsupported value resolver, Please use expression resolver");
        }, ValueExpressionResolver -> (String expression, Object parameter) -> {
            val context = new StandardEvaluationContext(parameter);
            return PARSER.parseExpression(expression).getValue(context, String.class);
        }));
        return timedAspect;
    }

    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        val countedAspect = new CountedAspect(registry);

        countedAspect.setMeterTagAnnotationHandler(new CountedMeterTagAnnotationHandler(resolverProvider -> {
            throw new UnsupportedOperationException("Unsupported value resolver, Please use expression resolver");
        }, ValueExpressionResolver -> (String expression, Object parameter) -> {
            val context = new StandardEvaluationContext(parameter);
            return PARSER.parseExpression(expression).getValue(context, String.class);
        }));
        return countedAspect;
    }
}
