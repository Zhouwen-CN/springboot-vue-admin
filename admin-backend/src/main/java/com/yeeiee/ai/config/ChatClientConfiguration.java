package com.yeeiee.ai.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.observation.DefaultChatClientObservationConvention;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-11-10
 */
@Configuration
public class ChatClientConfiguration {

    @Bean
    public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .maxMessages(100)
                .chatMemoryRepository(chatMemoryRepository)
                .build();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel, ObservationRegistry observationRegistry, ChatMemory chatMemory) {
        return ChatClient.builder(
                        openAiChatModel,
                        observationRegistry,
                        new DefaultChatClientObservationConvention()
                )
                // .defaultSystem()
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).order(0).build(),
                        new SimpleLoggerAdvisor(1)
                )
                .build();
    }

}
