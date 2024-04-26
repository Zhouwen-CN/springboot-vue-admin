package com.yeeiee;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Types;
import java.util.Collections;

@SpringBootTest
class MybatisGeneratorTests {
	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Test
	void generator() {
		val projectPath = System.getProperty("user.dir");

		FastAutoGenerator.create(url, username, password)
				.globalConfig(builder -> builder.author("chen")
						.enableSwagger()
						.outputDir(projectPath + "/src/main/java")
				)
				.dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
					int typeCode = metaInfo.getJdbcType().TYPE_CODE;
					if (typeCode == Types.SMALLINT) {
						// 自定义类型转换
						return DbColumnType.INTEGER;
					}
					return typeRegistry.getColumnType(metaInfo);
				}))
				.packageConfig(builder -> {
					builder.parent("com.yeeiee") // 设置父包名
							.entity("entity")
							.controller("controller")
							.mapper("mapper")
							.service("service")
							.serviceImpl("service.impl")
							.pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")); // 设置mapperXml生成路径
				})
				.strategyConfig(builder -> {
					builder.addInclude("t_data_field") // 设置需要生成的表名
							.addTablePrefix("t_", "c_"); // 设置过滤表前缀

					builder.entityBuilder()
							.enableRemoveIsPrefix()
							.enableTableFieldAnnotation()
							.enableLombok()
							.enableFileOverride();

					builder.controllerBuilder()
							.enableRestStyle()
							.enableFileOverride();

					builder.serviceBuilder()
							.formatServiceFileName("%sService")
							.formatServiceImplFileName("%sServiceImpl")
							.enableFileOverride();

					builder.mapperBuilder()
							.enableBaseResultMap()
							.formatMapperFileName("%sMapper")
							.formatXmlFileName("%sMapper")
							.enableFileOverride();

				})
				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}

}
