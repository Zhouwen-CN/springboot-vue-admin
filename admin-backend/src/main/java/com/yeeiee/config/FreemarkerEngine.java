package com.yeeiee.config;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.service.CodegenColumnService;
import com.yeeiee.service.CodegenTableService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-09-16
 */
@RequiredArgsConstructor
@Component
public class FreemarkerEngine implements InitializingBean {
    private final CodegenTableService codegenTableService;
    private final CodegenColumnService codegenColumnService;
    private Configuration configuration;

    @Override
    public void afterPropertiesSet() throws Exception {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(FreemarkerEngine.class, "/templates");
        configuration.setNumberFormat("#");
    }

    public Map<String, String> codegen(Long tableId) {
        val codegenTable = codegenTableService.lambdaQuery()
                .eq(CodegenTable::getId, tableId)
                .one();

        val codegenColumnList = codegenColumnService.lambdaQuery()
                .eq(CodegenColumn::getTableId, tableId)
                .list();

        val bindingMap = new HashMap<String, Object>();
        bindingMap.put("table", codegenTable);
        bindingMap.put("columns", codegenColumnList);


        return Map.of();
    }

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
}
