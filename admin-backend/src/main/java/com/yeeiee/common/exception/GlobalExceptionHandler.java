package com.yeeiee.common.exception;

import com.yeeiee.common.enumeration.RequestMethodEnum;
import com.yeeiee.common.utils.IPUtil;
import com.yeeiee.common.utils.JsonUtil;
import com.yeeiee.common.utils.RequestObjectUtil;
import com.yeeiee.common.utils.SecurityUserUtil;
import com.yeeiee.system.domain.entity.ErrorLog;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.service.ErrorLogService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
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

    /**
     * 404异常
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public R<Void> noResourceFoundExceptionHandler(NoResourceFoundException e) {
        return R.error(HttpStatus.NOT_FOUND, e);
    }

    /**
     * 事务超时异常
     */
    @ExceptionHandler(TransactionTimedOutException.class)
    public R<Void> transactionTimedOutExceptionHandler(TransactionTimedOutException e) {
        return R.error(HttpStatus.REQUEST_TIMEOUT, ExceptionUtils.getRootCauseMessage(e));
    }

    /**
     * body参数校验异常（请求实体）
     */
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

        return R.error(HttpStatus.UNPROCESSABLE_ENTITY, "请求体参数校验失败");
    }

    /**
     * url参数校验异常（接口参数注解）
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public R<Void> handlerMethodValidationExceptionHandler(HandlerMethodValidationException e) {
        val allValidationResults = e.getParameterValidationResults();
        if (CollectionUtils.isEmpty(allValidationResults)) {
            return R.error(HttpStatus.BAD_REQUEST, e.getReason());
        }
        val parameterValidationResult = allValidationResults.get(0);
        val parameterName = parameterValidationResult.getMethodParameter().getParameterName();
        val resolvableErrors = parameterValidationResult.getResolvableErrors();
        var defaultMessage = "请求参数校验失败";
        if (!CollectionUtils.isEmpty(resolvableErrors)) {
            defaultMessage = resolvableErrors.get(0).getDefaultMessage();
        }
        return R.error(HttpStatus.BAD_REQUEST, String.format("%s %s", parameterName, defaultMessage));
    }

    /**
     * dml操作异常
     */
    @ExceptionHandler(DmlOperationException.class)
    public R<Void> dmlFailureExceptionHandler(DmlOperationException e) {
        return R.error(HttpStatus.BAD_REQUEST, e);
    }


    /**
     * 代码生成异常
     */
    @ExceptionHandler(CodegenException.class)
    public R<Void> dataSourceConnectExceptionHandler(CodegenException e) {
        return R.error(HttpStatus.BAD_REQUEST, e);
    }

    /**
     * 定时任务异常：JobServiceImpl
     */
    @ExceptionHandler(JobSchedulerException.class)
    public R<Void> jobSchedulerExceptionHandler(JobSchedulerException e) {
        return R.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 刷新token异常
     */
    @ExceptionHandler(RefreshTokenException.class)
    public R<Void> refreshTokenExceptionHandler(RefreshTokenException e) {
        return R.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }


    /**
     * 默认异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> defaultExceptionHandler(Exception e) {
        this.saveErrorLog(e);
        return R.error(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private void saveErrorLog(Exception e) {
        val request = RequestObjectUtil.getHttpServletRequest();
        val user = SecurityUserUtil.getSecurityUser();
        val errorLog = new ErrorLog();
        errorLog.setUrl(request.getRequestURI());
        errorLog.setMethod(RequestMethodEnum.from(request.getMethod()));
        val parameterMap = RequestObjectUtil.getParameterMap(request);
        errorLog.setParams(JsonUtil.toJsonString(parameterMap));
        errorLog.setIp(IPUtil.getClientIP(request));
        errorLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        errorLog.setErrorMsg(ExceptionUtils.getStackTrace(e));
        errorLog.setCreateUser(user.getUsername());

        errorLogService.save(errorLog);
    }
}
