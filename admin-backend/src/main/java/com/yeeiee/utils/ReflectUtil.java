package com.yeeiee.utils;

import com.yeeiee.domain.form.FormToBeanHelper;
import lombok.val;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 * 反射工具类
 * </p>
 *
 * @author chen
 * @since 2025-06-03
 */
public final class ReflectUtil {
    /**
     * 获取 {@link FormToBeanHelper} 的泛型
     *
     * @param obj 当前类
     * @return 默认返回第一个
     * @throws RuntimeException not found
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getImplInterfaceT(FormToBeanHelper<T> obj) {
        val genericInterfaces = obj.getClass().getGenericInterfaces();

        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
                return (Class<T>) parameterizedType.getActualTypeArguments()[0];
            }
        }

        throw new RuntimeException("Can not get the generics that implement the interface");
    }
}
