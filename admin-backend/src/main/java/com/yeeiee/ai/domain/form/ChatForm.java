package com.yeeiee.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * AI聊天表单
 * </p>
 *
 * @author chen
 * @since 2025-11-11
 */
@Getter
@Setter
@ToString
@Schema(name = "ChatForm", description = "AI聊天表单")
public class ChatForm {
    @NotBlank
    @Schema(description = "模型")
    private String model;
    @NotBlank
    @Schema(description = "会话id")
    private String conversationId;
    @NotBlank
    @Schema(description = "提示词")
    private String prompt;
}
