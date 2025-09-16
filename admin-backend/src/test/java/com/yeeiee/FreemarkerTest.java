package com.yeeiee;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.yeeiee.domain.entity.CodegenColumn;
import com.yeeiee.domain.entity.CodegenTable;
import com.yeeiee.service.CodegenColumnService;
import com.yeeiee.service.CodegenTableService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-09-16
 */
@SpringBootTest
public class FreemarkerTest {

    private static Configuration configuration;
    @Autowired
    private CodegenTableService codegenTableService;
    @Autowired
    private CodegenColumnService codegenColumnService;
    private CodegenTable codegenTable;
    private List<CodegenColumn> codegenColumnList;

    @BeforeAll
    public static void init() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(ConstVal.UTF8);
        configuration.setClassForTemplateLoading(FreemarkerTest.class, "/templates");
        configuration.setNumberFormat("#");
    }

    @BeforeEach
    public void before() {
        codegenTable = codegenTableService.lambdaQuery()
                .eq(CodegenTable::getId, 3)
                .one();

        codegenColumnList = codegenColumnService.lambdaQuery()
                .eq(CodegenColumn::getTableId, 3)
                .list();
    }

    @Test
    public void entityRanderTest() {

        val entity = rander("domain/form/form.java.ftl", Map.of(
                "table", codegenTable,
                "columns", codegenColumnList
        ));

        System.out.println(entity);
    }

    public String rander(String ftlPath, Map<String, Object> bindingMap) {
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
