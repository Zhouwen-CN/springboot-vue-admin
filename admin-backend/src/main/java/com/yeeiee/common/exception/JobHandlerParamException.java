package com.yeeiee.common.exception;

import lombok.experimental.StandardException;

/**
 * <p>
 * 任务参数异常，不会进行重试
 * </p>
 *
 * @author chen
 * @since 2025-10-19
 */
@StandardException
public class JobHandlerParamException extends RuntimeException {
}
