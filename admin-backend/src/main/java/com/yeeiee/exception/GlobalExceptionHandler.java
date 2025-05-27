package com.yeeiee.exception;

import com.yeeiee.domain.entity.ErrorLog;
import com.yeeiee.service.ErrorLogService;
import com.yeeiee.utils.CommonUtil;
import com.yeeiee.utils.IPUtil;
import com.yeeiee.utils.JsonUtil;
import com.yeeiee.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorLogService errorLogService;

    @ExceptionHandler(DmlOperationException.class)
    public R<Void> dmlFailureExceptionHandler(DmlOperationException e) {
        return R.error(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(VerifyTokenException.class)
    public R<Void> verifyTokenExceptionHandler(VerifyTokenException e) {
        return R.error(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public R<Void> noResourceFoundExceptionHandler(NoResourceFoundException e) {
        return R.error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(PaginationSqlParseException.class)
    public R<Void> paginationSqlParseExceptionHandler(PaginationSqlParseException e) {
        return R.error(HttpStatus.BAD_REQUEST, String.format("分页SQL解析错误: %s", ExceptionUtils.getRootCauseMessage(e)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        val bindingResult = e.getBindingResult();

        val fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            return R.error(HttpStatus.BAD_REQUEST, String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()));
        }

        val globalError = bindingResult.getGlobalError();
        if (globalError != null) {
            return R.error(HttpStatus.BAD_REQUEST, globalError.getDefaultMessage());
        }

        return R.error(HttpStatus.BAD_REQUEST, "请求参数校验失败");
    }

    @ExceptionHandler(Exception.class)
    public R<Void> defaultExceptionHandler(Exception e) {
        this.saveErrorLog(e);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private void saveErrorLog(Exception e) {
        val request = CommonUtil.getHttpServletRequest();
        val user = CommonUtil.getSecurityUser();
        val errorLog = new ErrorLog();
        errorLog.setUsername(user.getUsername());
        errorLog.setUrl(request.getRequestURI());
        errorLog.setMethod(request.getMethod());
        val parameterMap = CommonUtil.getParameterMap(request);
        errorLog.setParams(JsonUtil.toJsonString(parameterMap));
        errorLog.setIp(IPUtil.getClientIP(request));
        errorLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        errorLog.setErrorMsg(ExceptionUtils.getStackTrace(e));

        errorLogService.save(errorLog);
    }
}
