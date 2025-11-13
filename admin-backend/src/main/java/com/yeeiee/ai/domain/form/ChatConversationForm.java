package com.yeeiee.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-11-13
 */
@Getter
@Setter
@ToString
@Schema(name = "ChatConversationForm", description = "AI对话表单")
public class ChatConversationForm {
    @NotBlank
    @Schema(description = "会话标题")
    private String title;
}
