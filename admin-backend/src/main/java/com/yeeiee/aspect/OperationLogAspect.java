package com.yeeiee.aspect;

import com.yeeiee.entity.OperationLog;
import com.yeeiee.enumeration.StatusEnum;
import com.yeeiee.service.OperationLogService;
import com.yeeiee.utils.CommonUtil;
import com.yeeiee.utils.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.val;
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

/**
 * {@code @Author:} chen
 * {@code @Date:} 2025/1/13
 * {@code @Desc:}
 */
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogService operationLogService;

    @Around("@annotation(operation)")
    public Object around(ProceedingJoinPoint pjp, Operation operation) throws Throwable {
        long beginTime = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            long time = System.currentTimeMillis() - beginTime;
            saveOperationLog(pjp, operation, StatusEnum.SUCCESS, time);

            return result;
        } catch (Throwable e) {
            long time = System.currentTimeMillis() - beginTime;
            saveOperationLog(pjp, operation, StatusEnum.FIELD, time);
            throw e;
        }
    }

    /**
     * 保存操作日志
     *
     * @param pjp        连接点
     * @param operation  操作注解，使用 swagger 自带的
     * @param statusEnum 状态
     * @param time       耗时
     */
    private void saveOperationLog(ProceedingJoinPoint pjp, Operation operation, StatusEnum statusEnum, long time) throws NoSuchMethodException {
        val httpServletRequest = CommonUtil.getHttpServletRequest();
        val targetClass = pjp.getTarget().getClass();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = targetClass.getDeclaredMethod(signature.getName(), signature.getParameterTypes());

        // 查询接口不记录日志
        val getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) {
            return;
        }

        val user = CommonUtil.getSecurityUser();
        val operationLog = new OperationLog();
        operationLog.setUsername(user.getUsername());
        operationLog.setOperation(operation.summary());
        operationLog.setUrl(httpServletRequest.getRequestURI());
        operationLog.setMethod(httpServletRequest.getMethod());
        String params = this.getParameter(method, pjp.getArgs());
        operationLog.setParams(params);
        operationLog.setTime(time);
        operationLog.setStatus(statusEnum.getStatus());
        operationLog.setIp(CommonUtil.getIpAddr(httpServletRequest));
        operationLog.setUserAgent(httpServletRequest.getHeader(HttpHeaders.USER_AGENT));

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
