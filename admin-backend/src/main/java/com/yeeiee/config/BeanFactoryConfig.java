package com.yeeiee.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>
 * bean 工厂后置处理器
 * </p>
 *
 * @author chen
 * @since 2025-06-20
 */
@Component
@Order
public class BeanFactoryConfig implements BeanFactoryPostProcessor {

    /**
     * 好像是 jpa @Projection 投影，用不到
     * <pre>
     * 会引起很多警告，选择直接排除
     * 参考：<a href="https://blog.csdn.net/WX10301075WX/article/details/124978403">CSDN</a>
     * </pre>
     */
    public static final String EXCLUDE_BEAN_NAME = "projectingArgumentResolverBeanPostProcessor";
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory factory) {
            if (factory.containsBeanDefinition(EXCLUDE_BEAN_NAME)) {
                factory.removeBeanDefinition(EXCLUDE_BEAN_NAME);
            }
        }
    }
}
