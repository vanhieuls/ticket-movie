package com.example.english.Controller.ChatBot;

import com.example.english.Dto.Chat.ChatRequest;
import com.example.english.Dto.Chat.ChatResponse;
import com.example.english.Service.ChatBotService.ChatService;
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
    ChatService chatService;
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.message() == null || request.message().trim().isEmpty()) {
//            return ResponseEntity.badRequest().body("Tin nhắn không được để trống.");
            ChatResponse errorRes = new ChatResponse(
                    "error",
                    "Tin nhắn không được để trống.",
                    null, null, null,
                    null, null, null, null,null
            );
            return ResponseEntity.badRequest().body(errorRes);
        }

//        String authenticatedUserId = principal.getName();
//        String chatId = "user:" + authenticatedUserId;
        ChatResponse response = chatService.getChatResponse(request.message(), "user:"+0);
//    String chatResponse = chatClient.prompt().system("Bạn là trợ ý ảo có tên là Hiếu đẹp trai")
//            .user(request.message())
//            .call()
//            .content();
//        return ResponseEntity.ok(chatResponse);
        return ResponseEntity.ok(response);
    }
}
