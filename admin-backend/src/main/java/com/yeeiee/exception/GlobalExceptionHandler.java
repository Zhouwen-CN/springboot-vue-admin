package com.yeeiee.exception;

import com.yeeiee.entity.ErrorLog;
import com.yeeiee.service.ErrorLogService;
import com.yeeiee.utils.CommonUtil;
import com.yeeiee.utils.JsonUtil;
import com.yeeiee.utils.R;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    ErrorLogService errorLogService;

    @ExceptionHandler(BadCredentialsException.class)
    public R<String> badCredentialsHandler() {
        return R.error(HttpStatus.FORBIDDEN, "用户名或密码错误");
    }

    @ExceptionHandler(AuthenticationException.class)
    public R<String> authenticationExceptionHandler(AuthenticationException e) {
        return R.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(DmlOperationException.class)
    public R<String> dmlFailureHandler(DmlOperationException e) {
        return R.error(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public R<String> noResourceFoundHandler(NoResourceFoundException e) {
        return R.error(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(Exception.class)
    public R<String> defaultHandler(Exception e) {
        this.saveErrorLog(e);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private void saveErrorLog(Exception e) {
        val request = CommonUtil.getHttpServletRequest();
        if (request != null) {
            val securityUser = CommonUtil.getSecurityUser();
            val errorLog = new ErrorLog();
            errorLog.setUsername(securityUser.getUsername());
            errorLog.setUrl(request.getRequestURI());
            errorLog.setMethod(request.getMethod());
            val parameterMap = CommonUtil.getParameterMap(request);
            errorLog.setParams(JsonUtil.toJsonString(parameterMap));
            errorLog.setIp(CommonUtil.getIpAddr(request));
            errorLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
            errorLog.setErrorMsg(ExceptionUtils.getStackTrace(e));

            errorLogService.save(errorLog);
        }
    }
}
