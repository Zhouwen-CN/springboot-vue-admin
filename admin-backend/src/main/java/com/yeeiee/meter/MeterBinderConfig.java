package com.yeeiee.meter;

import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.httpcomponents.hc5.PoolingHttpClientConnectionManagerMetricsBinder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 *  注册 {@link MeterBinder} 会自动绑定指标
 *  参考：{@link MetricsAutoConfiguration}
 * </pre>
 *
 * @author chen
 * @since 2025-08-21
 */
@Configuration
public class MeterBinderConfig {

    /**
     * http client5 连接池监控
     * @param poolingHttpClientConnectionManager 连接池
     * @return meter binder
     */
    @Bean
    public MeterBinder meterBinder(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager){
        return new PoolingHttpClientConnectionManagerMetricsBinder(poolingHttpClientConnectionManager, "sv-admin");
    }
}
