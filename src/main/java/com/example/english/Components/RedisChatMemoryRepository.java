package com.example.english.Components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class    RedisChatMemoryRepository implements ChatMemoryRepository {
    private static final Logger log = LoggerFactory.getLogger(RedisChatMemoryRepository.class);

    // Prefix KHÔNG đổi để không va chạm key domain khác
    private static final String KEY_PREFIX = "chat:memory:";
    // TTL mặc định 30 ngày
    private static final long TTL_SECONDS = 30L * 24 * 3600;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    public RedisChatMemoryRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    private String key(String conversationId) {
        return KEY_PREFIX + conversationId;
    }

    @Override
    public List<String> findConversationIds() {
        return redisTemplate.execute((RedisCallback<List<String>>) conn -> {
            var ids = new ArrayList<String>();
            ScanOptions opts = ScanOptions.scanOptions()
                    .match(KEY_PREFIX + "*")
                    .count(1_000)
                    .build();

            try (Cursor<byte[]> cur = conn.keyCommands().scan(opts)) {
                while (cur.hasNext()) {
                    String fullKey = new String(cur.next(), StandardCharsets.UTF_8);
                    // cắt đúng theo độ dài prefix (tránh split ":" lỗi)
                    ids.add(fullKey.substring(KEY_PREFIX.length()));
                }
            } catch (Exception e) {
                log.warn("SCAN keys failed", e);
            }
            return ids;
        });
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        var k = key(conversationId);

        List<Object> raw = redisTemplate.opsForList().range(k, 0, -1);
        if (raw == null || raw.isEmpty()) return List.of();

        List<Message> out = new ArrayList<>(raw.size());
        for (Object o : raw) {
            try {
                // serializer của bạn sẽ trả về String cho JSON string;
                // nếu không phải String (hiếm), ta stringify lại.
                String json = (o instanceof String) ? (String) o : mapper.writeValueAsString(o);
                JsonNode n = mapper.readTree(json);
                out.add(toMessage(n));
            } catch (Exception ex) {
                log.warn("Deserialize chat message failed: {}", o, ex);
            }
        }
        return out;
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Assert.notEmpty(messages, "messages cannot be empty");

        var k = key(conversationId);

        try {
            // ghi đè hoàn toàn: DEL rồi RPUSH
            redisTemplate.delete(k);

            List<String> jsons = new ArrayList<>(messages.size());
            for (Message m : messages) {
                // Chuẩn hóa thành JSON phẳng (messageType/text/metadata)
                Map<String, Object> node = new LinkedHashMap<>();
                node.put("messageType", m.getMessageType().getValue());
                node.put("text", m.getText());
                Map<String, Object> meta = new LinkedHashMap<>(m.getMetadata() == null ? Map.of() : m.getMetadata());
                meta.put("timestamp", Instant.now().toString());
                node.put("metadata", meta);
                jsons.add(mapper.writeValueAsString(node));
            }
            if (!jsons.isEmpty()) {
                // rightPushAll với Object template vẫn ổn vì value là String
                redisTemplate.opsForList().rightPushAll(k, new ArrayList<>(jsons));
            }
            redisTemplate.expire(k, TTL_SECONDS, TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Serialize chat messages failed", e);
        }
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        redisTemplate.delete(key(conversationId));
    }

    /* ======================= helpers ======================= */

    private Message toMessage(JsonNode node) {
        String type = optText(node, "messageType", MessageType.USER.getValue());
        MessageType mt;
        try {
            mt = MessageType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            mt = MessageType.USER;
        }
        String text = optText(node, "text", "");

        Map<String, Object> metadata = Optional.ofNullable(node.get("metadata"))
                .map(n -> mapper.convertValue(n, new TypeReference<Map<String, Object>>() {}))
                .orElseGet(HashMap::new);

        // đảm bảo có timestamp
        metadata.putIfAbsent("timestamp", Instant.now().toString());

        return switch (mt) {
            case USER -> UserMessage.builder().text(text).metadata(metadata).build();
            case ASSISTANT -> new AssistantMessage(text, metadata);
            case SYSTEM -> SystemMessage.builder().text(text).metadata(metadata).build();
            case TOOL -> new ToolResponseMessage(List.of(), metadata);
        };
    }

    private static String optText(JsonNode node, String field, String def) {
        JsonNode v = (node == null) ? null : node.get(field);
        return (v == null || v.isNull()) ? def : v.asText(def);
    }
}