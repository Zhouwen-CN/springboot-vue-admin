package com.yeeiee.aspect;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * <p>
 * 使用 aspectj 控制事务
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@Component
public class TransactionAspect {
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.yeeiee.service.impl.*.*(..))";

    /**
     * dao 层：insert delete update select
     * service 层：add remove modify get
     */
    @Bean
    public TransactionInterceptor transactionInterceptor(TransactionManager transactionManager) {
        DefaultTransactionAttribute txAttrRequired = new DefaultTransactionAttribute();
        txAttrRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        DefaultTransactionAttribute txAttrRequiredReadonly = new DefaultTransactionAttribute();
        txAttrRequiredReadonly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttrRequiredReadonly.setReadOnly(true);

        NameMatchTransactionAttributeSource transactionAttributeSource = new NameMatchTransactionAttributeSource();

        // read
        transactionAttributeSource.addTransactionalMethod("select*", txAttrRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("get*", txAttrRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("query*", txAttrRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("find*", txAttrRequiredReadonly);

        // write
        transactionAttributeSource.addTransactionalMethod("insert*", txAttrRequired);
        transactionAttributeSource.addTransactionalMethod("add*", txAttrRequired);
        transactionAttributeSource.addTransactionalMethod("save*", txAttrRequired);
        transactionAttributeSource.addTransactionalMethod("create*", txAttrRequired);
        transactionAttributeSource.addTransactionalMethod("delete*", txAttrRequired);
        transactionAttributeSource.addTransactionalMethod("remove*", txAttrRequired);
        transactionAttributeSource.addTransactionalMethod("update*", txAttrRequired);
        transactionAttributeSource.addTransactionalMethod("modify*", txAttrRequired);

        return new TransactionInterceptor(transactionManager, transactionAttributeSource);
    }

    @Bean
    public Advisor transactionAdviceAdvisor(TransactionInterceptor transactionInterceptor) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 定义切面
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, transactionInterceptor);
    }
}