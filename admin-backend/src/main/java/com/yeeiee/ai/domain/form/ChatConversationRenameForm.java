package com.yeeiee.ai.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * AI聊天会话重命名表单
 * </p>
 *
 * @author chen
 * @since 2025-11-12
 */
@Getter
@Setter
@ToString
@Schema(name = "ChatConversationRenameForm", description = "AI聊天会话重命名表单")
public class ChatConversationRenameForm {
    @NotBlank
    @Schema(description = "会话ID")
    private String conversationId;
    @NotBlank
    @Length(max = 255)
    @Schema(description = "标题")
    private String title;
}
