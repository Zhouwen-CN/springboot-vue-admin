package com.yeeiee.exception;

import com.yeeiee.domain.entity.ErrorLog;
import com.yeeiee.domain.vo.R;
import com.yeeiee.service.ErrorLogService;
import com.yeeiee.utils.IPUtil;
import com.yeeiee.utils.JsonUtil;
import com.yeeiee.utils.RequestObjectUtil;
import com.yeeiee.utils.SecurityUserUtil;
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
 * <pre>
 *     200：成功
 *     201：请求成功，并创建一个新的资源，通常是post、put请求相应
 *     202：请求已收到，但是未采取行动
 *     204：删除成功
 *     400：请求有误
 *     401：没有权限
 *     403：禁止访问
 *     404：资源不存在
 *     410：记录被删除
 *     422：参数错误
 *     500：服务器错误
 * </pre>
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
            return R.error(HttpStatus.UNPROCESSABLE_ENTITY, String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()));
        }

        val globalError = bindingResult.getGlobalError();
        if (globalError != null) {
            return R.error(HttpStatus.UNPROCESSABLE_ENTITY, globalError.getDefaultMessage());
        }

        return R.error(HttpStatus.UNPROCESSABLE_ENTITY, "请求参数校验失败");
    }

    @ExceptionHandler(Exception.class)
    public R<Void> defaultExceptionHandler(Exception e) {
        this.saveErrorLog(e);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private void saveErrorLog(Exception e) {
        val request = RequestObjectUtil.getHttpServletRequest();
        val user = SecurityUserUtil.getSecurityUser();
        val errorLog = new ErrorLog();
        errorLog.setUsername(user.getUsername());
        errorLog.setUrl(request.getRequestURI());
        errorLog.setMethod(request.getMethod());
        val parameterMap = RequestObjectUtil.getParameterMap(request);
        errorLog.setParams(JsonUtil.toJsonString(parameterMap));
        errorLog.setIp(IPUtil.getClientIP(request));
        errorLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        errorLog.setErrorMsg(ExceptionUtils.getStackTrace(e));

        errorLogService.save(errorLog);
    }
}
