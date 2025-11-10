package com.yeeiee.scheduler;

import com.yeeiee.exception.HttpStatusException;
import io.micrometer.observation.ObservationRegistry;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.observation.DefaultClientRequestObservationConvention;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * quartz 定时任务 restClient 配置
 * </p>
 *
 * @author chen
 * @since 2025-11-10
 */
@Configuration
public class QuartzRestClientConfiguration {

    private static final String REST_CLIENT_METER_NAME = "quartz.http.client.requests";

    @Bean("quartzRestClient")
    public RestClient restClient(RestClient.Builder builder, ObservationRegistry observationRegistry) {
        return builder
                .defaultStatusHandler(
                        (HttpStatusCode status) -> !status.is2xxSuccessful(),
                        (HttpRequest request, ClientHttpResponse response) -> {
                            val httpStatus = response.getStatusCode().value();
                            throw new HttpStatusException(httpStatus, new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
                        })
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention(REST_CLIENT_METER_NAME))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .build();
    }
}
