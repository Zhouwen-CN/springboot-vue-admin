package com.yeeiee.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.val;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * swagger 配置类
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    /**
     * 前端开发服务地址
     */
    private static final String DEV_SERVER_URL = "http://localhost:5173/dev";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, createAPIKeyScheme()))
                .info(new Info().title("Springboot-Vue-Admin")
                        .description("后台管理项目")
                        .version("1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                );
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * 添加前端开发服务地址
     * @return OpenApiCustomizer
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer(){
        return openApi -> {
            val servers = openApi.getServers();
            if (!servers.isEmpty()) {
                val server = servers.get(0);
                server.setDescription("生产环境");
            }

            val dev = new Server();
            dev.setUrl(DEV_SERVER_URL);
            dev.setDescription("开发环境");
            servers.add(dev);
        };
    }
}
