package com.yeeiee.config;

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
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
@Configuration
public class RestTemplateConfig {

    /**
     * 最大连接
     */
    @Value("${http.pool.max-total}")
    private Integer maxTotal;

    /**
     * 每个路由最大连接
     */
    @Value("${http.pool.max-per-route}")
    private Integer maxPerRoute;

    /**
     * 从连接池获取连接超时时间
     */
    @Value("${http.pool.obtain-connection-timeout}")
    private Duration obtainConnectionTimeout;

    /**
     * 连接在空闲 指定时间 后再次使用，需验证有效性
     */
    @Value("${http.pool.validate-after-inactivity}")
    private Duration validateAfterIdle;

    /**
     * 建立连接超时时间
     */
    @Value("${http.connect.connect-timeout}")
    private Duration connectTimeout;

    /**
     * 读取超时时间
     */
    @Value("${http.connect.socket-timeout}")
    private Duration socketTimeout;

    /**
     * 默认的保持连接时间，如果响应头没有的话
     */
    @Value("${http.connect.default-keep-alive}")
    private Duration defaultKeepAlive;


    @Bean
    @SneakyThrows
    public LayeredConnectionSocketFactory sslSocketFactory(){
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
        manager.setMaxTotal(maxTotal);
        // 设置每个路由默认并发数
        manager.setDefaultMaxPerRoute(maxPerRoute);

        manager.setDefaultConnectionConfig(
                ConnectionConfig.custom()
                        .setValidateAfterInactivity(TimeValue.of(validateAfterIdle))
                        .setConnectTimeout(Timeout.of(connectTimeout))
                        // 通过源码发现，等同于 RequestConfig.setResponseTimeout()，如果ResponseTimeout不为空的话
                        .setSocketTimeout(Timeout.of(socketTimeout))
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
                .setConnectionRequestTimeout(Timeout.of(obtainConnectionTimeout))
                .setDefaultKeepAlive(defaultKeepAlive.toMillis(), TimeUnit.MILLISECONDS)
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
    public RestTemplate restTemplate(HttpClient httpClient) {
        val factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(connectTimeout);
        return new RestTemplate(factory);
    }
}
