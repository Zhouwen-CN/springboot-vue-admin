package com.yeeiee.service.freemarker;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.service.CodegenColumnService;
import com.yeeiee.service.CodegenTableService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * freemarker 引擎
 * </p>
 *
 * @author chen
 * @since 2025-09-17
 */
@RequiredArgsConstructor
@Service
public class FreemarkerEngineService implements InitializingBean {
    private static final PackageConfig packageConfig = new PackageConfig();
    private final CodegenTableService codegenTableService;
    private final CodegenColumnService codegenColumnService;
    private Configuration configuration;

    /**
     * 初始化 freemarker
     *
     * @throws Exception ex
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(FreemarkerEngineService.class, "/templates");
        configuration.setNumberFormat("#");
    }

    /**
     * 根据代码生成表id，生成代码
     *
     * @param tableId 代码生成表id
     * @return key -> 文件地址；value -> 文件内容
     */
    public Map<String, String> codegenById(Long tableId) {
        val table = codegenTableService.lambdaQuery()
                .eq(CodegenTable::getId, tableId)
                .one();

        val columns = codegenColumnService.lambdaQuery()
                .eq(CodegenColumn::getTableId, tableId)
                .list();

        val bindingMap = new HashMap<String, Object>();
        bindingMap.put("table", table);
        bindingMap.put("columns", columns);
        val packageInfo = packageConfig.getPackageInfo();
        bindingMap.put("package", packageInfo);

        val values = JavaTemplate.values();
        val result = new HashMap<String, String>();
        for (JavaTemplate value : values) {
            val ftlPath = value.getFtlPath();
            val content = this.render(ftlPath, bindingMap);
            val filePath = value.getFilePath(table.getClassName());
            result.put(filePath, content);
        }

        return result;
    }

    /**
     * 根据模板文件生成文件内容
     *
     * @param ftlPath    模板文件地址
     * @param bindingMap 绑定变量
     * @return 文件内容
     */
    private String render(String ftlPath, Map<String, Object> bindingMap) {
        try {
            val template = configuration.getTemplate(ftlPath);
            val stringWriter = new StringWriter();
            template.process(bindingMap, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * java模板，获取模板文件地址和生成文件地址（枚举名称为模板前缀）
     */
    public enum JavaTemplate {
        entity(className -> packageConfig.javaFilePath + "/domain/entity/" + className + ".java"),
        vo(className -> packageConfig.javaFilePath + "/domain/vo/" + className + "Vo.java"),
        form(className -> packageConfig.javaFilePath + "/domain/form/" + className + "Form.java"),
        mapper(className -> packageConfig.javaFilePath + "/mapper/" + className + "Mapper.java"),
        service(className -> packageConfig.javaFilePath + "/service/" + className + "Service.java"),
        serviceImpl(className -> packageConfig.javaFilePath + "/service/impl/" + className + "ServiceImpl.java"
        ),
        controller(className -> packageConfig.javaFilePath + "/controller/" + className + "Controller.java"),
        xml(className -> packageConfig.xmlFilePath + "/" + className + "Mapper.xml");

        private static final String TEMPLATE_PATH_PREFIX = "java";
        private final Function<String, String> function;

        JavaTemplate(Function<String, String> function) {
            this.function = function;
        }

        private String getFtlPath() {
            if (JavaTemplate.xml == this) {
                return TEMPLATE_PATH_PREFIX + "/mapper.xml.ftl";
            }
            return TEMPLATE_PATH_PREFIX + "/" + name() + ".java.ftl";
        }

        private String getFilePath(String className) {
            return function.apply(className);
        }
    }

    /**
     * js模板，获取模板文件地址和生成文件地址（枚举名称为模板前缀）
     */
    public enum JsTemplate {
        index(businessName -> packageConfig.jsFilePath + "/views/" + businessName + "/index.vue"),
        form(businessName -> packageConfig.jsFilePath + "/views/" + businessName + "/components/" + StringUtils.capitalize(businessName) + "Form.vue"),
        api(businessName -> packageConfig.jsFilePath + "/api/" + businessName + "/index.ts");

        private static final String TEMPLATE_PATH_PREFIX = "js";
        private final Function<String, String> func;

        JsTemplate(Function<String, String> func) {
            this.func = func;
        }


        private String getFtlPath() {
            if (JsTemplate.api == this) {
                return TEMPLATE_PATH_PREFIX + "/" + name() + ".ts.ftl";
            }
            return TEMPLATE_PATH_PREFIX + "/" + name() + ".vue.ftl";
        }

        private String getFilePath(String businessName) {
            return func.apply(businessName);
        }
    }

    /**
     * 包配置
     */
    public static class PackageConfig {
        public String basePackage = "com.yeeiee";
        public String entity = "domain.entity";
        public String vo = "domain.vo";
        public String form = "domain.form";
        public String mapper = "mapper";
        public String service = "service";
        public String serviceImpl = "service.impl";
        public String controller = "controller";
        public String javaFilePath = "admin-backend/src/main/java/" + basePackage.replace(".", "/");
        public String xmlFilePath = "admin-backend/src/main/resources/mapper";
        public String jsFilePath = "admin-frontend/src";

        public Map<String, String> getPackageInfo() {
            return Map.of(
                    "basePackage", basePackage,
                    "entity", basePackage + "." + entity,
                    "vo", basePackage + "." + vo,
                    "form", basePackage + "." + form,
                    "mapper", basePackage + "." + mapper,
                    "service", basePackage + "." + service,
                    "serviceImpl", basePackage + "." + serviceImpl,
                    "controller", basePackage + "." + controller
            );
        }
    }
}
