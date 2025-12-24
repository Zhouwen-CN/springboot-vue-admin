package com.yeeiee.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * AI对话记录表
 * </p>
 *
 * @author chen
 * @since 2025-11-11
 */
@Getter
@Setter
@ToString
@TableName("t_chat_history")
@KeySequence("t_chat_history_seq")
public class ChatHistory {

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 会话id
     */
    @TableField(value = "conversation_id")
    private String conversationId;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}