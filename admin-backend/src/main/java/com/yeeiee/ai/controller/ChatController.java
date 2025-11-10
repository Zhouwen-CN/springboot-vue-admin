package com.yeeiee.ai.controller;

import com.yeeiee.security.JwtTokenProvider;
import com.yeeiee.utils.RequestObjectUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
@Tag(name = "聊天 控制器")
public class ChatController {

    private final ChatClient chatClient;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/chat")
    public Flux<String> chatStream(@RequestParam(value = "prompt") String prompt, HttpServletRequest request) {

        val token = RequestObjectUtil.getTokenFromRequest(request);
        val userId = jwtTokenProvider.parseAccessToken(token)
                .map(claimsJws -> claimsJws.getPayload().getSubject())
                .orElse("unknown");

        return chatClient.prompt(prompt).advisors(
                memoryAdvisor -> memoryAdvisor.param(ChatMemory.CONVERSATION_ID, userId)
        ).stream().content();
    }
}
