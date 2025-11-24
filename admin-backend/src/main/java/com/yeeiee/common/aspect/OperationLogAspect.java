package com.yeeiee.common.aspect;

import com.yeeiee.common.enumeration.OperationStatusEnum;
import com.yeeiee.common.enumeration.RequestMethodEnum;
import com.yeeiee.common.utils.IPUtil;
import com.yeeiee.common.utils.JsonUtil;
import com.yeeiee.common.utils.RequestObjectUtil;
import com.yeeiee.common.utils.SecurityUserUtil;
import com.yeeiee.system.domain.entity.OperationLog;
import com.yeeiee.system.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Optional;

/**
 * <p>
 * 操作日志切面类
 * </p>
 *
 * @author chen
 * @since 2025-01-13
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class OperationLogAspect {

    private final OperationLogService operationLogService;

    @Around("@annotation(operation)")
    public Object around(ProceedingJoinPoint pjp, Operation operation) throws Throwable {
        long beginTime = System.currentTimeMillis();

        // 控制器名称
        val controllerName = Optional.ofNullable(pjp.getTarget().getClass().getAnnotation(Tag.class))
                .map(Tag::name)
                .orElse(StringUtils.EMPTY);

        // 操作名称
        val operationValue = controllerName + "-" + operation.summary();

        try {
            Object result = pjp.proceed();
            long time = System.currentTimeMillis() - beginTime;
            saveOperationLog(pjp, operationValue, OperationStatusEnum.SUCCESS, time);
            log.info("【{}】-【{}】 success in {}ms", controllerName, operation.summary(), time);
            return result;
        } catch (Throwable e) {
            long time = System.currentTimeMillis() - beginTime;
            saveOperationLog(pjp, operationValue, OperationStatusEnum.FAILURE, time);
            log.warn("【{}】-【{}】 failure in {}ms: {}", controllerName, operation.summary(), time, ExceptionUtils.getRootCauseMessage(e));
            throw e;
        }
    }

    /**
     * 保存操作日志
     *
     * @param pjp                 连接点
     * @param operation           操作注解，使用 swagger 自带的
     * @param operationStatusEnum 状态
     * @param time                耗时
     */
    private void saveOperationLog(ProceedingJoinPoint pjp, String operation, OperationStatusEnum operationStatusEnum, long time) throws NoSuchMethodException {
        val httpServletRequest = RequestObjectUtil.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        val method = signature.getMethod();

        // 查询接口不记录日志
        val getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            return;
        }

        val user = SecurityUserUtil.getSecurityUser();
        val operationLog = new OperationLog();
        operationLog.setOperation(operation);
        operationLog.setUrl(httpServletRequest.getRequestURI());
        operationLog.setMethod(RequestMethodEnum.from(httpServletRequest.getMethod()));
        String params = this.getParameter(method, pjp.getArgs());
        operationLog.setParams(params);
        operationLog.setTime(time);
        operationLog.setStatus(operationStatusEnum);
        operationLog.setIp(IPUtil.getClientIP(httpServletRequest));
        operationLog.setUserAgent(httpServletRequest.getHeader(HttpHeaders.USER_AGENT));
        operationLog.setCreateUser(user.getUsername());

        operationLogService.save(operationLog);
    }


    /**
     * 如果是 RequestBody 参数，则直接返回<br/>
     * 如果是 其他参数，则封装成map
     *
     * @param method 请求方法
     * @param args   请求参数
     * @return 请求参数
     */
    private String getParameter(Method method, Object[] args) {
        val parameters = method.getParameters();
        val requestParamMap = new HashMap<String, Object>();
        for (int i = 0; i < parameters.length; i++) {
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            String key = parameters[i].getName();
            if (requestBody != null) {
                return JsonUtil.toJsonString(args[i]);
            } else {
                requestParamMap.put(key, args[i]);
            }
        }

        return JsonUtil.toJsonString(requestParamMap);
    }
}
