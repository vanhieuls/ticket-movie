package com.example.english.Components;

import com.example.english.Service.ChatBotService.RagIngestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@Slf4j
public class RagDataLoader implements ApplicationRunner {
    RagIngestionService ragIngestionService;
    VectorStore vectorStore;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("RagDataLoader chạy khi khởi động ứng dụng...");
        log.info("Bắt đầu nạp dữ liệu RAG vào Vector Store bằng ApplicationRunner...");
        try {
            String filter = "type == 'movie' || type == 'cinema' || type == 'screen' || type == 'screen_type'";
            vectorStore.delete(filter);
            int count = ragIngestionService.ingestAll();
            log.info(" Hoan thanh nap du lieu RAG. Tong cong da nap {} du lieu.", count);
        } catch (Exception e) {
            log.error("❌ Lỗi nghiêm trọng khi nạp dữ liệu RAG vào Vector Store: {}", e.getMessage(), e);
        }
    }

}
