package com.yeeiee.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
@OpenAPIDefinition(info = @Info(
        title = "meta tool",
        description = "hive 元数据工具",
        version = "1.0.0",
        license = @License(
                name = "Apache 2.0"
        )
))
public class OpenApiConfig {
}
