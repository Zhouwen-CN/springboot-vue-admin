package com.yeeiee.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.yeeiee.controller.BaseController;
import lombok.val;

import java.sql.Types;
import java.util.Collections;

/**
 * <p>
 * mybatis 代码生成，运行时需要包含 provided 依赖
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
public final class MybatisGeneratorUtil {

    public static void generator(String... tableName) {
        val url = "jdbc:mysql://localhost:3306/springboot_vue_admin";
        val username = "root";
        val password = "123";
        val projectPath = System.getProperty("user.dir") + "\\admin-backend";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder.author("chen")
                        .enableSpringdoc()
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
                    builder.addInclude(tableName) // 设置需要生成的表名
                            .addTablePrefix("t_"); // 设置过滤表前缀

                    builder.entityBuilder()
                            .javaTemplate("/templates/entity.java")
                            .idType(IdType.AUTO)
                            .enableRemoveIsPrefix()
                            .enableTableFieldAnnotation()
                            .enableLombok()
                            .disableSerialVersionUID()
                            .addTableFills(
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.UPDATE)
                            );
                    //.enableFileOverride();

                    builder.controllerBuilder()
                            .template("/templates/controller.java")
                            .superClass(BaseController.class)
                            .enableRestStyle()
                            .enableFileOverride();

                    builder.serviceBuilder()
                            .formatServiceFileName("%sService");
                    //.enableFileOverride();

                    builder.mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList();
                    //.enableFileOverride();

                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    public static void main(String[] args) {
        generator("t_data_field", "t_data_range", "t_data_storey", "t_menu", "t_role", "t_role_menu", "t_root_word", "t_user", "t_user_role");
    }
}
