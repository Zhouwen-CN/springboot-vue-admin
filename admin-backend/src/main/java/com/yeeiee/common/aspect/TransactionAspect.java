package com.yeeiee.common.aspect;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;
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
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        val signature = (MethodSignature) pjp.getSignature();
        val targetClass = pjp.getTarget().getClass();

        // 检查是否标注了 @NonTransaction
        if (this.hasNonTransactionAnnotation(signature, targetClass)) {
            return pjp.proceed();
        }

        val signatureName = signature.getName();
        val isReadOnly = READ_ONLY_PATTERN.matcher(signatureName).matches();
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); // 默认就是0
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

    /**
     * 检查方法或类上是否有 @NonTransaction 注解
     * @param signature 方法签名
     * @param targetClass 目标类
     * @return boolean
     */
    private boolean hasNonTransactionAnnotation(MethodSignature signature, Class<?> targetClass) {
        Method method = signature.getMethod();

        // 检查实现类方法上的注解
        if (method.isAnnotationPresent(NonTransaction.class)) {
            return true;
        }

        // 检查实现类上的注解
        if (targetClass.isAnnotationPresent(NonTransaction.class)) {
            return true;
        }

        // 如果方法来自接口
        Class<?>[] interfaces = targetClass.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            try {

                // 检查接口方法上的注解
                Method interfaceMethod = interfaceClass.getMethod(method.getName(), method.getParameterTypes());
                if (interfaceMethod.isAnnotationPresent(NonTransaction.class)) {
                    return true;
                }

                // 检查接口类上的注解
                if (interfaceClass.isAnnotationPresent(NonTransaction.class)) {
                    return true;
                }
            } catch (NoSuchMethodException e) {
                // do nothing
            }
        }

        return false;
    }
}
