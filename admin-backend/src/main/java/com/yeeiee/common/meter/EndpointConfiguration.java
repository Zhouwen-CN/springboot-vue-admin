package com.yeeiee.common.meter;

import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * actuator 端点配置，生产环境建议自定义 Repository
 * </p>
 *
 * @author chen
 * @since 2025-09-01
 */
// @Configuration
public class EndpointConfiguration {

    /**
     * audit events 端点
     * @return audit event repository
     */
    @Bean
    public AuditEventRepository auditEventRepository(){
        return new InMemoryAuditEventRepository();
    }


    /**
     * http exchanges 端点
     * @return http exchange repository
     */
    @Bean
    public HttpExchangeRepository httpExchangeRepository(){
        return new InMemoryHttpExchangeRepository();
    }
}
