package com.yeeiee.scheduler.handler.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.yeeiee.exception.JobHandlerException;
import com.yeeiee.exception.JobHandlerParamException;
import com.yeeiee.scheduler.handler.AbstractJobHandler;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * http任务处理类
 * </p>
 *
 * @author chen
 * @since 2025-10-16
 */
@Service
public class HttpJobHandler extends AbstractJobHandler {

    public HttpJobHandler(@Qualifier("quartzRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    private final RestClient restClient;

    // 支持的Content-Type，默认application/json
    private final Set<String> SUPPORT_CONTENT_TYPE = Set.of(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE
    );

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
                    val key = entry.getKey();
                    val value = entry.getValue();
                    if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(key)) {
                        if (!SUPPORT_CONTENT_TYPE.contains(value)) {
                            throw new JobHandlerParamException("Content-Type仅支持: " + SUPPORT_CONTENT_TYPE);
                        }
                        if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(value)) {
                            httpConfig.setIsForm(true);
                        }
                    }
                    headers.add(key, value);
                }
            }
        });

        val body = httpConfig.getBody();
        if (body != null) {
            if (httpConfig.getIsForm()) {
                val linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
                val fieldNames = body.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    val value = body.get(fieldName).asText();
                    linkedMultiValueMap.add(fieldName, value);
                }
                request.body(linkedMultiValueMap);
            } else {
                request.body(body);
            }
        }

        try {
            return request.retrieve().body(String.class);
        } catch (Exception e) {
            throw new JobHandlerException(String.format("请求异常: %s", e.getMessage()));
        }
    }

    @Override
    public String name() {
        return "http";
    }

    /**
     * <pre>
     * http请求配置类，示例：
     * {@code {
     *   "method": "post",
     *   "url": "http://127.0.0.1:8080/test",
     *   "headers": {
     *     "Authorization": "Bearer token"
     *   },
     *   "body": {
     *     "name": "name",
     *     "age": 18
     *   }
     * }}
     * </pre>
     */
    @Getter
    @Setter
    @ToString
    public static class HttpConfig {
        @NotBlank
        @com.yeeiee.scheduler.domain.validate.HttpMethod
        private String method;
        @NotBlank
        private String url;
        private Map<String, String> headers;
        private JsonNode body;
        @JsonIgnore
        private Boolean isForm = false;
    }
}
