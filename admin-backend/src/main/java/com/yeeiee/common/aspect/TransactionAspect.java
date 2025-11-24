package com.yeeiee.common.aspect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.regex.Pattern;

/**
 * <p>
 * 使用 aop 控制事务
 * </p>
 *
 * @author chen
 * @since 2025-07-15
 */
@Aspect
@Component
@RequiredArgsConstructor
public class TransactionAspect {
    private final TransactionTemplate transactionTemplate;
    private final Pattern READ_ONLY_PATTERN = Pattern.compile("^(get|select|list|find).*$");

    @Around("execution (public * com.yeeiee..service.impl.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) {
        val signature = pjp.getSignature();
        val signatureName = signature.getName();

        val isReadOnly = READ_ONLY_PATTERN.matcher(signatureName).matches();
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.setReadOnly(isReadOnly);
        transactionTemplate.setName(signatureName);

        // 鉴权过程中的异常，比如事务超时，会抛到控制台
        return transactionTemplate.execute(new TransactionCallback<>() {
            @SneakyThrows
            @Override
            public Object doInTransaction(TransactionStatus status) {
                return pjp.proceed();
            }
        });
    }
}
