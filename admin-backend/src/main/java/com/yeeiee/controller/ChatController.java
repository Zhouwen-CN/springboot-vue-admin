package com.yeeiee.controller;

import io.micrometer.observation.ObservationRegistry;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
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
@RestController
@RequestMapping("/ai")
@Tag(name = "聊天 控制器")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(OpenAiChatModel openAiChatModel, ObservationRegistry observationRegistry){
        chatClient = ChatClient.builder(openAiChatModel, observationRegistry, null)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "prompt",defaultValue = "你是谁？") String prompt){
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/chat/stream")
    public Flux<String> chatStream(@RequestParam(value = "prompt",defaultValue = "你是谁？") String prompt){
        return chatClient.prompt( prompt).stream().content();
    }
}
