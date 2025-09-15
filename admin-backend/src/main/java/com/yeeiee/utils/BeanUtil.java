package com.yeeiee.utils;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * <pre>
 * 转换对象，比如
 *   DO 转 VO
 *   Form 转 DO
 * 数据量不大，凑合着用，如果数据量大需要考虑上 MapStruct
 * </pre>
 *
 * @author chen
 * @since 2025-09-15
 */
public final class BeanUtil {
    @SneakyThrows
    public static <S, T> T toBean(S source, Class<T> clazz) {
        val constructor = clazz.getConstructor();
        val target = constructor.newInstance();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <S, T> List<T> toBean(List<S> source, Class<T> target) {
        return source.stream().map(item -> toBean(item, target)).toList();
    }
}
