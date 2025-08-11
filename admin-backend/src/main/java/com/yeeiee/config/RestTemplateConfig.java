package com.yeeiee.config;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.NoOpResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 暂时还用不到，后面很可能会用到
 * </p>
 *
 * @author chen
 * @since 2025-05-23
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "http")
public class RestTemplateConfig {

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
        private Duration obtainConnectionTimeout = Duration.ofSeconds(5);
    }

    @Getter
    @Setter
    public static class ConnectConfig{
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
    }


    @Bean
    @SneakyThrows
    public LayeredConnectionSocketFactory sslSocketFactory() {
        // 1. 创建信任所有证书的 SSLContext
        // 使用 HttpClient 5 提供的 TrustAllStrategy
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, TrustAllStrategy.INSTANCE) // 关键：加载信任所有策略
                .build();

        // 2. 创建 SSLConnectionSocketFactory，允许所有主机名
        // NoopHostnameVerifier.INSTANCE 禁用主机名验证
        return SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE) // 关键：禁用主机名验证
                .build();
    }


    /**
     * http连接池
     *
     * @return 连接池
     */
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager(LayeredConnectionSocketFactory sslSocketFactory) {
        val manager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        // 最大连接数
        manager.setMaxTotal(this.pool.maxTotal);
        // 设置每个路由默认并发数
        manager.setDefaultMaxPerRoute(this.pool.maxPerRoute);

        manager.setDefaultConnectionConfig(
                ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.of(this.connect.connectTimeout))
                        // 通过源码发现，等同于 RequestConfig.setResponseTimeout()，如果ResponseTimeout不为空的话
                        .setSocketTimeout(Timeout.of(this.connect.socketTimeout))
                        .build()
        );

        return manager;
    }

    /**
     * 配置请求参数
     */
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(this.pool.obtainConnectionTimeout))
                .setDefaultKeepAlive(this.connect.defaultKeepAlive.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * http客户端
     *
     * @param httpClientConnectionManager 客户端连接池
     * @param requestConfig               请求配置
     * @return 客户端
     * 设置默认请求头,拦截器啥的
     */
    @Bean
    public HttpClient httpClient(HttpClientConnectionManager httpClientConnectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(httpClientConnectionManager)
                // 定时清除过期的连接
                .evictExpiredConnections()
                // 禁用自动重试
                .disableAutomaticRetries()
                // 禁用重定向
                .disableRedirectHandling()
                // 不设置会有一个默认的 DefaultConnectionKeepAliveStrategy
                // .setKeepAliveStrategy()
                .build();
    }

    /**
     * 请求模版类
     *
     * @param httpClient http客户端
     * @return 模版类
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilderConfigurer restTemplateBuilderConfigurer, HttpClient httpClient) {
        val factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(this.connect.connectTimeout);

        var builder = new RestTemplateBuilder();
        builder = restTemplateBuilderConfigurer.configure(builder);

        return builder.requestFactory(() -> factory)
                // 默认4xx，5xx会抛出异常
                .errorHandler(new NoOpResponseErrorHandler())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8")
                .build();
    }
}
