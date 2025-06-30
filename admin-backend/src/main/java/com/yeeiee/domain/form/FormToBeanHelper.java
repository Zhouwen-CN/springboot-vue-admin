package com.yeeiee.domain.form;

import com.yeeiee.utils.ReflectUtil;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * from to bean接口
 * <pre>
 *     1.bean 需要有一个无参构造
 *     2.form 需要实现 {@link FormToBeanHelper} 接口，接口的泛型就是需要转换的 bean 类型
 * </pre>
 * </p>
 *
 * @author chen
 * @since 2025-06-03
 */
public interface FormToBeanHelper<T> {
    @SneakyThrows
    default T toBean() {
        val implInterfaceT = ReflectUtil.getImplInterfaceT(this);
        val constructor = implInterfaceT.getConstructor();
        val t = constructor.newInstance();
        BeanUtils.copyProperties(this, t);
        return t;
    }
}
