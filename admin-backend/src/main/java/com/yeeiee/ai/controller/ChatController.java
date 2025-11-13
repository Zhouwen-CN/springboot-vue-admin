package com.yeeiee.ai.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yeeiee.ai.domain.entity.ChatHistory;
import com.yeeiee.ai.domain.form.ChatConversationRenameForm;
import com.yeeiee.ai.domain.vo.ChatHistoryVo;
import com.yeeiee.ai.service.ChatHistoryService;
import com.yeeiee.exception.AiChatException;
import com.yeeiee.system.domain.vo.R;
import com.yeeiee.system.security.JwtTokenProvider;
import com.yeeiee.utils.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-11-07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
@Tag(name = "AI聊天 控制器")
public class ChatController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatClient chatClient;
    private final ChatHistoryService chatHistoryService;
    private final ChatMemoryRepository chatMemoryRepository;

    @PostMapping(value = "/chat", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestHeader("chatId") String chatId,
            @RequestBody @NotBlank String prompt
    ) {
        val userId = jwtTokenProvider.getUserIdByAccessToken(token);

        val exists = chatHistoryService.exists(
                Wrappers.<ChatHistory>lambdaQuery()
                        .eq(ChatHistory::getUserId, userId)
                        .eq(ChatHistory::getConversationId, chatId)
        );
        if (!exists) {
            throw new AiChatException("聊天会话不存在: " + userId + ":" + chatId);
        }
        return chatClient.prompt(prompt)
                .advisors(memoryAdvisor -> memoryAdvisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .chatResponse()
                .map(chatResponse -> ServerSentEvent.<String>builder()
                        .data(chatResponse.getResult().getOutput().getText())
                        .event("message")
                        .build()
                );
    }

    @Operation(summary = "获取聊天历史")
    @GetMapping("/chat/history")
    public R<List<ChatHistoryVo>> getChatHistory(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        val userId = jwtTokenProvider.getUserIdByAccessToken(token);
        val list = chatHistoryService.lambdaQuery()
                .eq(ChatHistory::getUserId, userId)
                .orderByDesc(ChatHistory::getUpdateTime)
                .list();
        val voList = BeanUtil.toBean(list, ChatHistoryVo.class);
        return R.ok(voList);
    }

    @Operation(summary = "创建聊天会话")
    @PostMapping(value = "/chat/conversation", consumes = MediaType.TEXT_PLAIN_VALUE)
    public R<String> createChatConversation(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody @NotBlank String title
    ) {
        val userId = jwtTokenProvider.getUserIdByAccessToken(token);
        val conversationId = String.valueOf(System.currentTimeMillis());
        val chatHistory = new ChatHistory();
        chatHistory.setUserId(userId);
        chatHistory.setConversationId(conversationId);
        chatHistory.setTitle(title);
        chatHistoryService.save(chatHistory);
        return R.ok(conversationId);
    }

    @Operation(summary = "获取聊天会话")
    @GetMapping("/chat/history/{conversationId}")
    public R<List<Message>> getChatConversation(@PathVariable("conversationId") String conversationId) {
        val messageList = chatMemoryRepository.findByConversationId(conversationId);
        return R.ok(messageList);
    }

    @Operation(summary = "重命名聊天会话")
    @PatchMapping("/chat/conversation")
    public R<Void> modifyChatConversationTitle(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody @Validated ChatConversationRenameForm form
    ) {
        val userId = jwtTokenProvider.getUserIdByAccessToken(token);
        chatHistoryService.lambdaUpdate()
                .eq(ChatHistory::getUserId, userId)
                .eq(ChatHistory::getConversationId, form.getConversationId())
                .set(ChatHistory::getTitle, form.getTitle())
                .update();

        return R.ok();
    }

    @Operation(summary = "删除聊天会话")
    @DeleteMapping("/chat/conversation/{conversationId}")
    public R<Void> removeChatConversation(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("conversationId") String conversationId
    ) {
        chatMemoryRepository.deleteByConversationId(conversationId);
        val userId = jwtTokenProvider.getUserIdByAccessToken(token);
        chatHistoryService.remove(
                Wrappers.<ChatHistory>lambdaQuery()
                        .eq(ChatHistory::getUserId, userId)
                        .eq(ChatHistory::getConversationId, conversationId)
        );

        return R.ok();
    }
}
