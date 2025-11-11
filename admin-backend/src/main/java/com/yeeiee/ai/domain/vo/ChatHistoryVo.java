package com.yeeiee.ai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * AI聊天记录视图
 * </p>
 *
 * @author chen
 * @since 2025-11-11
 */
@Getter
@Setter
@ToString
@Schema(name = "ChatHistoryVo", description = "AI聊天记录视图")
public class ChatHistoryVo {

    @Schema(description = "会话id")
    private String conversationId;

    @Schema(description = "标题")
    private String title;
}