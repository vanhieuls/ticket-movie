package com.example.english.Service.Implement;

import com.example.english.Dto.TxnInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisService {
    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper;
    String BLACKLIST_PREFIX = "jwt:blacklist:";
    String seatKey(long showtimeId, long seatId) {
        return "seat:showtime:" + showtimeId + ":seat:" + seatId;
    }
    private String txnKey(String vnp_TxnRef) {
        return "txnInfo:vnp_TxnRef:" + vnp_TxnRef;
    }
    public void blackList(String jwt, Instant exp) {
        if(jwt == null || exp == null) return;
        long  ttlMillis =  exp.toEpochMilli() - Instant.now().toEpochMilli();
        if (ttlMillis <= 0) ttlMillis = 1000;
        String key = BLACKLIST_PREFIX + jwt;
        redisTemplate.opsForValue().set(key,"1", ttlMillis, TimeUnit.MILLISECONDS);
    }
    public boolean isBlackList(String jwt) {
        if(jwt == null || jwt.isEmpty()) return false;
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jwt));
    }
    public void seatHolding (Long showTimeId, Long seatId, long ttl){
        if(showTimeId == null || seatId == null) return;
        String key = seatKey(showTimeId, seatId);
        redisTemplate.opsForValue().set(key,"1", ttl, TimeUnit.MINUTES);
    }
    public boolean isSeatHoldingList(Long showTimeId, Long seatId){
        if(showTimeId == null || seatId == null) return false;
        return Boolean.TRUE.equals(redisTemplate.hasKey(seatKey(showTimeId, seatId)));
    }
    public void deleteSeatHold(Long showTimeId, Long seatId){
        if(showTimeId == null || seatId == null) return;
        String key = seatKey(showTimeId, seatId);
        redisTemplate.delete(key);
    }
    public void txnRefInfo(String vnp_TxnRef, TxnInfo txnInfo, long ttl) {
        if(vnp_TxnRef == null || txnInfo == null) return;
        String txnKey = txnKey(vnp_TxnRef);
        redisTemplate.opsForValue().set(txnKey, txnInfo, ttl, TimeUnit.MINUTES);
    }

    public TxnInfo getByTxnRef(String vnp_TxnRef) {
        if (vnp_TxnRef == null || vnp_TxnRef.isBlank()) return null;

        Object raw = redisTemplate.opsForValue().get(txnKey(vnp_TxnRef));
        if (raw == null) return null;

        if (raw instanceof TxnInfo) return (TxnInfo) raw; // trường hợp có type-info
        try {
            return objectMapper.convertValue(raw, TxnInfo.class); // Map -> TxnInfo
        } catch (IllegalArgumentException e) {
            log.warn("Cannot convert redis value for key {} to TxnInfo. Type={}", txnKey(vnp_TxnRef), raw.getClass(), e);
            return null;
        }
    }
}
