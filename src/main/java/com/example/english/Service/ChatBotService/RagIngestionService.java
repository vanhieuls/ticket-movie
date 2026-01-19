package com.example.english.Service.ChatBotService;

import com.example.english.Components.RagDocumentMapper;
import com.example.english.Repository.CinemaRepository;
import com.example.english.Repository.MovieRepository;
import com.example.english.Repository.ScreenRoomRepository;
import com.example.english.Repository.ScreenRoomTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RagIngestionService {
    MovieRepository movieRepository;
    CinemaRepository cinemaRepository;
    ScreenRoomRepository screenRoomRepository;
    ScreenRoomTypeRepository screenRoomTypeRepository;
    RagDocumentMapper mapper;
    VectorStore vectorStore;
    TokenTextSplitter splitter = new TokenTextSplitter();
    @Transactional(readOnly = true)
    public int ingestAll() {
        int count = 0;
        count += ingestMovies();
        count += ingestCinemas();
        count += ingestScreens();
        count += ingestScreenTypes();
        return count;
    }
 // ddoạn này nếu quẻyr theo optinal sẽ lỗi chỗ mapper, đồng thời xem xét cách mapp vậy có làm chậm hệ thống không
    public int ingestMovies() {
        List<Document> docs = movieRepository.findTop100ByOrderByEndDateDesc()
                .stream()
                .map(mapper::fromMovie)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }

    public int ingestCinemas() {
        var docs = cinemaRepository.findAll()
                .stream()
                .map(mapper::fromCinema)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }

    public int ingestScreens() {
        var docs = screenRoomRepository.findAll()
                .stream()
                .map(mapper::fromScreenRoom)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }

    public int ingestScreenTypes() {
        var docs = screenRoomTypeRepository.findAll()
                .stream()
                .map(mapper::fromScreenRoomType)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }
}
