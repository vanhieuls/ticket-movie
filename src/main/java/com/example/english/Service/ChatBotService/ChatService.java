package com.example.english.Service.ChatBotService;

import com.example.english.Dto.Chat.ChatResponse;
import com.example.english.Service.ToolFunction.RagFilterBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatService {
    ChatClient chatClient;
    String systemPrompt = ChatSystemPrompt.cinemaJsonPrompt();
    public ChatResponse getChatResponse(String userMessage, String chatId) {
        final String filter = RagFilterBuilder.buildFilterExpression(userMessage);
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .advisors(a -> {
                    a.param(ChatMemory.CONVERSATION_ID, chatId);
                    a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, filter);
                })
                .call()
                .entity(ChatResponse.class);
    }
}
