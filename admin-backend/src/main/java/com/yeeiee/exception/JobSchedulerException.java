package com.yeeiee.exception;

import lombok.experimental.StandardException;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-10-13
 */
@StandardException
public class JobSchedulerException extends RuntimeException {
    @Override
    public String getMessage() {
        val cause = getCause();
        if (cause == null) {
            return super.getMessage();
        } else {
            return super.getMessage() + " " + ExceptionUtils.getRootCauseMessage(cause);
        }
    }
}
