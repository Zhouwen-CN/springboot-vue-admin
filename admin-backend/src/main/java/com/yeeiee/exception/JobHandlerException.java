package com.yeeiee.exception;

import lombok.experimental.StandardException;

/**
 * <p>
 * 任务处理异常，会根据配置进行重试
 * </p>
 *
 * @author chen
 * @since 2025-10-17
 */
@StandardException
public class JobHandlerException extends RuntimeException {
}
