package com.yeeiee.scheduler.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeeiee.exception.JobHandlerParamException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

/**
 * <p>
 * 任务处理器抽象类
 * </p>
 *
 * @author chen
 * @since 2025-10-17
 */

public abstract class AbstractJobHandler implements JobHandler {
    protected Validator validator;
    protected ObjectMapper objectMapper;

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 解析对象
     *
     * @param json  json字符串
     * @param clazz 对象类
     * @param <T>   对象类型
     * @return 对象
     */
    protected <T> T readValue(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new JobHandlerParamException(String.format("处理器参数解析异常: %s", e.getMessage()));
        }
    }

    /**
     * 校验对象
     *
     * @param obj 校验对象
     */
    protected void validate(Object obj) {
        val errors = validator.validateObject(obj);
        if (errors.hasErrors()) {
            val fieldError = errors.getFieldError();
            if (fieldError != null) {
                throw new JobHandlerParamException(
                        String.format("处理器参数校验失败: %s %s",
                                fieldError.getField(),
                                fieldError.getDefaultMessage())
                );
            }

            val globalError = errors.getGlobalError();
            if (globalError != null) {
                throw new JobHandlerParamException(
                        String.format("处理器参数校验失败: %s", globalError.getDefaultMessage())
                );
            }
        }
    }
}
