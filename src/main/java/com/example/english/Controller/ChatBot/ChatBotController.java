package com.example.english.Controller.ChatBot;

import com.example.english.Dto.Chat.ChatRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/chatbot")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ChatBot Controller", description = "APIs for ChatBot interactions")
public class ChatBotController {
    ChatClient chatClient;
    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
        if (request.message() == null || request.message().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tin nhắn không được để trống.");
        }

//        String authenticatedUserId = principal.getName();
//        String chatId = "user:" + authenticatedUserId;

    String chatResponse = chatClient.prompt().system("Bạn là trợ ý ảo có tên là Hiếu đẹp trai")
            .user(request.message())
            .call()
            .content();
        return ResponseEntity.ok(chatResponse);

    }
}
