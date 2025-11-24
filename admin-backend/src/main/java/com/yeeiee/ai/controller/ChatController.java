package com.yeeiee.ai.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yeeiee.ai.domain.entity.ChatHistory;
import com.yeeiee.ai.domain.form.ChatConversationRenameForm;
import com.yeeiee.ai.domain.vo.ChatHistoryVo;
import com.yeeiee.ai.service.ChatHistoryService;
import com.yeeiee.common.utils.BeanUtil;
import com.yeeiee.common.utils.SecurityUserUtil;
import com.yeeiee.system.domain.vo.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * AI聊天 控制器
 * </p>
 *
 * @author chen
 * @since 2025-11-07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/chat")
@Tag(name = "AI聊天 控制器")
public class ChatController {

    private final ChatClient chatClient;
    private final ChatHistoryService chatHistoryService;
    private final ChatMemoryRepository chatMemoryRepository;
    // 和前端约定好的换行符转义
    private static final String LINE_ESCAPE = "\\x0a";
    // 和前端约定好的空格符转义
    private static final String SPACE_ESCAPE = "\\x20";

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestHeader("chatId") String chatId,
            @RequestBody @NotBlank String prompt,
            HttpServletRequest request
    ) {

        // 保存 security 上下文到请求属性，不然上下文丢失会报错，因为会多次调用 security 过滤器链
        request.setAttribute(
                RequestAttributeSecurityContextRepository.DEFAULT_REQUEST_ATTR_NAME,
                SecurityContextHolder.getContext()
        );

        val userId = SecurityUserUtil.getSecurityUser().getId();
        val exists = chatHistoryService.exists(
                Wrappers.<ChatHistory>lambdaQuery()
                        .eq(ChatHistory::getUserId, userId)
                        .eq(ChatHistory::getConversationId, chatId)
        );
        if (!exists) {
            return Flux.just(
                    ServerSentEvent.<String>builder()
                            .event("error")
                            .data("聊天会话不存在: " + chatId)
                            .build()
            );
        }
        return chatClient.prompt(prompt)
                .advisors(memoryAdvisor -> memoryAdvisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .chatResponse()
                .filter(chatResponse -> Objects.nonNull(chatResponse.getResult().getOutput().getText()))
                .map(chatResponse -> {
                    val text = chatResponse.getResult().getOutput().getText()
                            .transform(s -> s.replace("\n", LINE_ESCAPE))
                            .transform(s -> s.replace(" ", SPACE_ESCAPE));
                            return ServerSentEvent.<String>builder()
                                    .event("message")
                                    .data(text)
                                    .build();
                        }
                );
    }

    @Operation(summary = "获取聊天历史")
    @GetMapping("/history")
    public R<List<ChatHistoryVo>> getChatHistory() {
        val userId = SecurityUserUtil.getSecurityUser().getId();
        val list = chatHistoryService.lambdaQuery()
                .eq(ChatHistory::getUserId, userId)
                .orderByDesc(ChatHistory::getUpdateTime)
                .list();
        val voList = BeanUtil.toBean(list, ChatHistoryVo.class);
        return R.ok(voList);
    }

    @Operation(summary = "创建聊天会话")
    @PostMapping(value = "/conversation", consumes = MediaType.TEXT_PLAIN_VALUE)
    public R<String> createChatConversation(@RequestBody @NotBlank String title) {
        val userId = SecurityUserUtil.getSecurityUser().getId();
        val conversationId = String.valueOf(System.currentTimeMillis());
        val chatHistory = new ChatHistory();
        chatHistory.setUserId(userId);
        chatHistory.setConversationId(conversationId);
        chatHistory.setTitle(title);
        chatHistoryService.save(chatHistory);
        return R.ok(conversationId);
    }

    @Operation(summary = "获取聊天会话")
    @GetMapping("/history/{conversationId}")
    public R<List<Message>> getChatConversation(@PathVariable("conversationId") String conversationId) {
        val messageList = chatMemoryRepository.findByConversationId(conversationId);
        return R.ok(messageList);
    }

    @Operation(summary = "重命名聊天会话")
    @PatchMapping("/conversation")
    public R<Void> modifyChatConversationTitle(@RequestBody @Validated ChatConversationRenameForm form) {
        val userId = SecurityUserUtil.getSecurityUser().getId();
        chatHistoryService.lambdaUpdate()
                .eq(ChatHistory::getUserId, userId)
                .eq(ChatHistory::getConversationId, form.getConversationId())
                .set(ChatHistory::getTitle, form.getTitle())
                .update();

        return R.ok();
    }

    @Operation(summary = "删除聊天会话")
    @DeleteMapping("/conversation/{conversationId}")
    public R<Void> removeChatConversation(@PathVariable("conversationId") String conversationId) {
        chatMemoryRepository.deleteByConversationId(conversationId);
        val userId = SecurityUserUtil.getSecurityUser().getId();
        chatHistoryService.remove(
                Wrappers.<ChatHistory>lambdaQuery()
                        .eq(ChatHistory::getUserId, userId)
                        .eq(ChatHistory::getConversationId, conversationId)
        );

        return R.ok();
    }
}
