package com.yeeiee.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * AI对话表单
 * </p>
 *
 * @author chen
 * @since 2025-12-24
 */
@Getter
@Setter
@ToString
@Schema(name = "ChatForm", description = "AI对话表单")
public class ChatForm {
    @NotBlank
    @Schema(description = "会话id")
    private String conversationId;
    @NotBlank
    @Schema(description = "提示词")
    private String prompt;
    @NotNull
    @Schema(description = "开启思考")
    private Boolean enableThinking;
    @NotNull
    @Schema(description = "开启搜索")
    private Boolean enableSearch;
}
