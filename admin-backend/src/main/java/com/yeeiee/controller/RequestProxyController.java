package com.yeeiee.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * <pre>
 * 请求代理 控制器，通过配置执行一些逻辑（最好是js脚本之类的，避免频繁重启服务）
 * 比如：
 *  1、对请求参数、请求头、请求体的新增、修改
 *  2、对响应体的处理
 * </pre>
 *
 * @author chen
 * @since 2025-09-28
 */
@Slf4j
@Deprecated
@RequiredArgsConstructor
@RestController
@Tag(name = "请求代理 控制器")
public class RequestProxyController {
    private final RestClient restClient;
    private static final String server = "http://127.0.0.1:8080";

    @RequestMapping(value = "/proxy/**")
    public void proxy(
            @RequestParam MultiValueMap<String, String> params,
            @RequestHeader MultiValueMap<String, String> headers,
            @RequestBody(required = false) String body,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        // headers.remove("authorization");
        headers.remove("host");

        // 请求路径
        val url = request.getRequestURI().replaceFirst("/proxy", "");
        // 请求方法
        val method = request.getMethod();

        // 请求参数、请求头转发
        val requestBodySpec = restClient.method(HttpMethod.valueOf(method))
                .uri(server + url, uriBuilder -> {
                    uriBuilder.queryParams(params);
                    return uriBuilder.build();
                })
                .headers((httpHeaders) -> httpHeaders.addAll(headers));

        // 请求体转发
        // 1、form
        if (request instanceof MultipartHttpServletRequest multipartHttpServletRequest) {
            val form = new LinkedMultiValueMap<String, Object>();
            val fileMap = multipartHttpServletRequest.getFileMap();
            val entries = fileMap.entrySet();
            for (Map.Entry<String, MultipartFile> entry : entries) {
                val multipartFile = entry.getValue();
                form.add(entry.getKey(), this.getInputStreamResource(multipartFile));
            }
            requestBodySpec.body(form);
        }

        // 2、json
        if (StringUtils.hasText(body)) {
            requestBodySpec.body(body);
        }

        // 发送请求
        ResponseEntity<byte[]> entity = requestBodySpec
                .retrieve()
                .toEntity(byte[].class);
        if (!entity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("请求失败");
        }

        // 响应头转发
        val entityHeaders = entity.getHeaders();
        val entrySet = entityHeaders.entrySet();
        for (val entry : entrySet) {
            val valueList = entry.getValue();
            for (String value : valueList) {
                // 同名不覆盖，追加
                response.addHeader(entry.getKey(), value);
            }
        }

        // 响应体转发
        val entityBody = Optional.ofNullable(entity.getBody()).orElse(new byte[0]);
        val outputStream = response.getOutputStream();
        outputStream.write(entityBody);
        outputStream.flush();
    }


    @RequestMapping("/test")
    public String test(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest multipartHttpServletRequest) {
            System.out.println("multipartHttpServletRequest = " + multipartHttpServletRequest);
        }
        System.out.println("request = " + request);
        return "ok";
    }


    private AbstractResource getInputStreamResource(MultipartFile multipartFile) {
        try {
            return new InputStreamResource(multipartFile.getInputStream()) {
                @Override
                public String getFilename() {
                    return multipartFile.getOriginalFilename();
                }

                @Override
                public long contentLength() throws IOException {
                    return multipartFile.getSize();
                }
            };
        } catch (Exception e) {
            log.error("Get InputStreamResource error: {}", e.getMessage());
        }

        return new ByteArrayResource(new byte[0]);
    }
}
