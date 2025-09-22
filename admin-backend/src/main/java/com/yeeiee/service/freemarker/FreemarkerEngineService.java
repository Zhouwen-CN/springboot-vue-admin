package com.yeeiee.service.freemarker;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.domain.entity.Menu;
import com.yeeiee.exception.CodegenFailedException;
import com.yeeiee.service.CodegenColumnService;
import com.yeeiee.service.CodegenTableService;
import com.yeeiee.service.MenuService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    private final MenuService menuService;
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

        if(table == null){
            throw new CodegenFailedException("代码生成表不存在");
        }

        val columns = codegenColumnService.lambdaQuery()
                .eq(CodegenColumn::getTableId, tableId)
                .list();

        if (CollectionUtils.isEmpty(columns)) {
            throw new CodegenFailedException("代码生成表字段不存在");
        }

        val bindingMap = new HashMap<String, Object>();
        bindingMap.put("table", table);
        bindingMap.put("columns", columns);
        val jsBasePackage = this.getJsBasePackage(table.getParentMenuId(), table.getBusinessName());
        val packageInfo = packageConfig.getPackageInfo(jsBasePackage);
        bindingMap.put("package", packageInfo);

        // java
        val javaTemplates = JavaTemplate.values();
        val result = new HashMap<String, String>();
        for (JavaTemplate value : javaTemplates) {
            val ftlPath = value.getFtlPath();
            val content = this.render(ftlPath, bindingMap);
            val filePath = value.getFilePath(table.getClassName());
            result.put(filePath, content);
        }

        // js
        val jsTemplates = JsTemplate.values();
        for (JsTemplate value : jsTemplates) {
            val ftlPath = value.getFtlPath();
            val content = this.render(ftlPath, bindingMap);
            val filePath = value.getFilePath(jsBasePackage);
            result.put(filePath, content);
        }

        return result;
    }

    /**
     * 获取js基础包名
     * @param parentMenuId 父级菜单id
     * @param businessName 业务名
     * @return js基础包名
     */
    private String getJsBasePackage(Long parentMenuId, String businessName) {
        val menu = menuService.lambdaQuery()
                .select(Menu::getAccessPath)
                .eq(Menu::getId, parentMenuId)
                .one();

        String jsBasePackage;
        if (menu == null) {
            jsBasePackage = "/" + businessName;
        } else {
            jsBasePackage = menu.getAccessPath() + "/" + businessName;
        }

        return jsBasePackage;
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
        index(jsBasePackage -> packageConfig.jsFilePath + "/views" + jsBasePackage + "/index.vue"),
        form(jsBasePackage -> packageConfig.jsFilePath + "/views" + jsBasePackage + "/components/" + getComponentName(jsBasePackage)),
        api(jsBasePackage -> packageConfig.jsFilePath + "/api" + jsBasePackage + "/index.ts");

        private static final String TEMPLATE_PATH_PREFIX = "js";
        private final Function<String, String> func;

        JsTemplate(Function<String, String> func) {
            this.func = func;
        }

        /**
         * 获取form组件名称
         * @param jsBasePackage js基础包名
         * @return form组件名称
         */
        private static String getComponentName(String jsBasePackage) {
            val split = jsBasePackage.split("/");
            return StringUtils.capitalize(split[split.length - 1]) + "Form.vue";
        }

        private String getFtlPath() {
            if (JsTemplate.api == this) {
                return TEMPLATE_PATH_PREFIX + "/" + name() + ".ts.ftl";
            }
            return TEMPLATE_PATH_PREFIX + "/" + name() + ".vue.ftl";
        }

        private String getFilePath(String jsBasePackage) {
            return func.apply(jsBasePackage);
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

        public Map<String, String> getPackageInfo(String jsBasePackage) {
            return Map.of(
                    "javaBasePackage", basePackage,
                    "entity", basePackage + "." + entity,
                    "vo", basePackage + "." + vo,
                    "form", basePackage + "." + form,
                    "mapper", basePackage + "." + mapper,
                    "service", basePackage + "." + service,
                    "serviceImpl", basePackage + "." + serviceImpl,
                    "controller", basePackage + "." + controller,
                    "jsBasePackage", jsBasePackage
            );
        }
    }
}
