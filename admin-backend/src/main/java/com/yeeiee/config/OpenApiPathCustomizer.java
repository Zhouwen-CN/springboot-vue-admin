package com.yeeiee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import lombok.val;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 修改 swagger-ui 配置，为了让前端开发环境走代理
 * <pre>
 *     1. 添加前端代理前缀
 *     2. 设置前端开发地址
 * </pre>
 * </p>
 *
 * @author chen
 * @since 2025-04-24
 */
@Component
@Profile("!prod")
public class OpenApiPathCustomizer implements OpenApiCustomizer {
    /**
     * 前端开发服务地址
     */
    private static final String DEV_SERVER_URL = "http://localhost:5173";
    /**
     * 代理前缀
     */
    private static final String PATH_PREFIX = "/dev";

    @Override
    public void customise(OpenAPI openApi) {
        // 请求路径添加前端代理前缀
        val newPaths = new Paths();
        openApi.getPaths().forEach((k, v) -> newPaths.addPathItem(PATH_PREFIX + k, v));
        openApi.setPaths(newPaths);

        // 设置前端开发的地址
        val servers = openApi.getServers();
        if (!servers.isEmpty()) {
            val server = servers.get(0);
            server.setUrl(DEV_SERVER_URL);
        }
    }
}
