package com.yeeiee.scheduler.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.yeeiee.exception.JobHandlerException;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * <p>
 * http任务处理类
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
@RequiredArgsConstructor
@Service
public class HttpJobHandler extends AbstractJobHandler {

    private final RestClient restClient;

    @Getter
    @Setter
    @ToString
    public static class HttpConfig {
        @NotBlank
        @com.yeeiee.domain.validate.HttpMethod
        private String method;
        @NotBlank
        private String url;
        private Map<String, String> headers;
        private JsonNode data;
    }

    @Override
    public String name() {
        return "http";
    }

    @Override
    public String execute(String param) {
        val httpConfig = readValue(param, HttpConfig.class);
        validate(httpConfig);

        val request = restClient.method(HttpMethod.valueOf(httpConfig.getMethod().toUpperCase()))
                .uri(httpConfig.getUrl());

        request.headers(headers -> {
            val headerMap = httpConfig.getHeaders();
            if (!CollectionUtils.isEmpty(headerMap)) {
                val entries = headerMap.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    headers.add(entry.getKey(), entry.getValue());
                }
            }
        });

        val data = httpConfig.getData();
        if (data !=null) {
            request.body(data);
        }

        try {
            return request.retrieve().body(String.class);
        } catch (Exception e) {
            throw new JobHandlerException(String.format("请求异常: %s", e.getMessage()));
        }
    }
}
