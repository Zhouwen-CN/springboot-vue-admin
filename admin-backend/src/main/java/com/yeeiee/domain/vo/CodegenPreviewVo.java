package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 代码生成预览视图
 * </p>
 *
 * @author chen
 * @since 2025-09-18
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenPreviewVo", description = "代码生成预览视图")
public class CodegenPreviewVo {
    @Schema(description = "文件路径")
    private String filePath;
    @Schema(description = "文件内容")
    private String content;

    public static List<CodegenPreviewVo> fromMap(Map<String, String> map) {
        return map.entrySet().stream().map(entry -> {
            val codegenPreviewVo = new CodegenPreviewVo();
            codegenPreviewVo.setFilePath(entry.getKey());
            codegenPreviewVo.setContent(entry.getValue());
            return codegenPreviewVo;
        }).toList();
    }
}
