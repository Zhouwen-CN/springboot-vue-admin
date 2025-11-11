package com.yeeiee.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * <pre>
 * rest client hc5 配置
 * 每一个 {@link RestClient.Builder} 都具有下面相同的配置
 * </pre>
 *
 * @author chen
 * @since 2025-08-19
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "custom.http.rest-client")
public class RestClientConfiguration {

    private PoolConfig pool = new PoolConfig();
    private ConnectConfig connect = new ConnectConfig();

    @Getter
    @Setter
    public static class PoolConfig {
        /**
         * 最大连接，默认100
         */
        private Integer maxTotal = 100;

        /**
         * 每个路由最大连接，默认10
         */
        private Integer maxPerRoute = 10;

        /**
         * 从连接池获取连接超时时间，默认5s
         */
        private Duration obtainConnectTimeout = Duration.ofSeconds(5);
    }

    @Getter
    @Setter
    public static class ConnectConfig {
        /**
         * 建立连接超时时间，默认5s
         */
        private Duration connectTimeout = Duration.ofSeconds(5);

        /**
         * 读取超时时间，默认60s
         */
        private Duration socketTimeout = Duration.ofSeconds(60);

        /**
         * 默认的保持连接时间，如果响应头没有的话，默认60s
         */
        private Duration defaultKeepAlive = Duration.ofSeconds(60);

        /**
         * 默认2秒，如果连接最后更新时间 + 2秒 < 当前时间，则会校验连接
         */
        private Duration validateAfterTime = Duration.ofSeconds(2);
    }

    /**
     * 连接池配置
     */
    private Consumer<PoolingHttpClientConnectionManagerBuilder> connectionManagerCustomizer() {
        return connectionManagerCustomizer -> connectionManagerCustomizer
                // 最大连接数
                .setMaxConnTotal(pool.getMaxTotal())
                // 设置每个路由默认并发数
                .setMaxConnPerRoute(pool.getMaxPerRoute())
                .setDefaultConnectionConfig(
                        ConnectionConfig.custom()
                                .setValidateAfterInactivity(Timeout.of(connect.getValidateAfterTime()))
                                .setConnectTimeout(Timeout.of(connect.getConnectTimeout()))
                                .setSocketTimeout(Timeout.of(connect.getSocketTimeout()))
                                // 连接最大存活时间，没有默认值，不设置不生效
                                // .setTimeToLive()
                                .build()
                );
    }

    /**
     * http客户端配置
     */
    private Consumer<HttpClientBuilder> httpClientCustomizer() {
        return httpClientCustomizer -> httpClientCustomizer
                // 定时清除过期的连接（线程默认休眠5秒）
                .evictExpiredConnections()
                // 禁用一些用不到的模块
                .disableRedirectHandling()
                .disableAutomaticRetries()
                .disableCookieManagement()
                .disableAuthCaching()
                .disableConnectionState();
    }

    /**
     * 默认请求配置
     */
    private Consumer<RequestConfig.Builder> defaultRequestConfigCustomizer() {
        return requestConfigCustomizer -> requestConfigCustomizer
                .setConnectionRequestTimeout(Timeout.of(pool.getObtainConnectTimeout()))
                .setDefaultKeepAlive(connect.getDefaultKeepAlive().toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * http client配置
     */
    @Bean
    public ClientHttpRequestFactoryBuilder<?> clientHttpRequestFactoryBuilder() {
        return ClientHttpRequestFactoryBuilder
                .httpComponents()
                .withConnectionManagerCustomizer(connectionManagerCustomizer())
                .withHttpClientCustomizer(httpClientCustomizer())
                .withDefaultRequestConfigCustomizer(defaultRequestConfigCustomizer());
    }
}
