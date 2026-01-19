package com.example.english.Configuration;

import com.example.english.Components.RedisChatMemoryRepository;
import com.example.english.Service.ToolFunction.ShowTimeTool;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiConfig {
    ShowTimeTool showtimeTool;
//    @Bean
//    public ChatClient chatClient(ChatModel chatModel) {
//        return ChatClient.builder(chatModel)
////                .defaultAdvisors(messageAdvisor, questionAnswerAdvisor)
////                .defaultTools(showtimeTool)
//                .build();
//    }
    @Bean
    public ChatMemory chatMemory(RedisChatMemoryRepository redisChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(30)
                .build();
    }

    @Bean
    public MessageChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory) {
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

    @Bean
    public QuestionAnswerAdvisor questionAnswerAdvisor(VectorStore vectorStore) {
        SearchRequest defaultSearch = SearchRequest.builder()
                .topK(20)
                .similarityThreshold(0.70)
                .build();

        return QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(defaultSearch)
                .build();
    }


    @Bean
    public ChatClient chatClient(ChatModel chatModel, MessageChatMemoryAdvisor messageAdvisor, QuestionAnswerAdvisor questionAnswerAdvisor) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(messageAdvisor, questionAnswerAdvisor)
                .defaultTools(showtimeTool)
                .build();
    }
}
