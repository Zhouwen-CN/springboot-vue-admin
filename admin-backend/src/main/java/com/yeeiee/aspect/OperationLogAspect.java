package com.yeeiee.aspect;

import com.yeeiee.entity.OperationLog;
import com.yeeiee.enumeration.StateEnum;
import com.yeeiee.service.OperationLogService;
import com.yeeiee.utils.CommonUtil;
import com.yeeiee.utils.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * {@code @Author:} chen
 * {@code @Date:} 2025/1/13
 * {@code @Desc:}
 */
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;

    @Around("@annotation(operation)")
    public Object around(ProceedingJoinPoint pjp, Operation operation) throws Throwable {
        long beginTime = System.currentTimeMillis();

        try {

            Object result = pjp.proceed();
            long time = System.currentTimeMillis() - beginTime;
            saveOperationLog(pjp, operation, StateEnum.SUCCESS, time);

            return result;
        } catch (Throwable e) {

            long time = System.currentTimeMillis() - beginTime;
            saveOperationLog(pjp, operation, StateEnum.FIELD, time);
            throw e;
        }
    }

    /**
     * 保存操作日志
     *
     * @param pjp       连接点
     * @param operation 操作注解，使用 swagger 自带的
     * @param stateEnum 状态
     * @param time      耗时
     */
    private void saveOperationLog(ProceedingJoinPoint pjp, Operation operation, StateEnum stateEnum, long time) {
        val httpServletRequest = CommonUtil.getHttpServletRequest();
        val user = CommonUtil.getSecurityUser();

        val operationLog = new OperationLog();
        operationLog.setUsername(user.getUsername());
        operationLog.setOperation(operation.summary());
        operationLog.setUrl(httpServletRequest.getRequestURI());
        operationLog.setMethod(httpServletRequest.getMethod());
        operationLog.setTime(time);
        operationLog.setStatus(stateEnum.getState());
        operationLog.setIp(CommonUtil.getIpAddr(httpServletRequest));
        operationLog.setUserAgent(httpServletRequest.getHeader(HttpHeaders.USER_AGENT));

        Object[] args = pjp.getArgs();
        try {
            String params = JsonUtil.toJsonString(args[0]);
            operationLog.setParams(params);
        } catch (Exception e) {
            // Do nothing
        }

        operationLogService.save(operationLog);
    }
}
