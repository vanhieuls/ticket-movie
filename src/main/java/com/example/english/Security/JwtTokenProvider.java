package com.example.english.Security;

import com.example.english.Enum.TypeToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import static com.example.english.Enum.TypeToken.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secretKey}")
    String secretKey;
    @Value("${security.jwt.token.expiration}")
    long expirationDate;
    @Value("${security.jwt.token.refreshKey}")
    String refreshKey;
    @Value("${security.jwt.token.expiration_refresh}")
    long expirationRefreshDate;
    @Value("${security.jwt.token.tempKey}")
    String tempKey;
    @Value("${security.jwt.token.expiration_temp}")
    long expirationTempDate;
    public String generateToken(UserDetails userDetails) {
        return deloyGenerateToken(userDetails, expirationDate, ACCESS_TOKEN);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return deloyGenerateToken(userDetails, expirationRefreshDate, REFRESH_TOKEN);
    }

    public String generateTempToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date currentDate = new Date();
        Date expireDate = new Date(new Date().getTime() + expirationTempDate);
        return Jwts.builder()
                .setId(UUID.randomUUID().toString()) //id token: Dùng để thu hồi khi cần
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key(TEMP_TOKEN))
                .compact();
    }

    public String deloyGenerateToken(UserDetails userDetails, long expireTime, TypeToken typeToken) {
        String username = userDetails.getUsername();
        Date currentDate = new Date();
        Date expireDate = new Date(new Date().getTime() + expireTime);
        String scope = buildScope(userDetails);
        return Jwts.builder()
                .setId(UUID.randomUUID().toString()) //id token: Dùng để thu hồi khi cần
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .claim("scope", scope) // User roles/authorities
                .signWith(key(typeToken))
                .compact();
    }

    private String buildScope(UserDetails userDetails) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(userDetails.getAuthorities())) {
            userDetails.getAuthorities().forEach(authority -> {
                stringJoiner.add(authority.getAuthority());
            });
        }
        return stringJoiner.toString();
    }

    private Key key(TypeToken typeKey) {
        if (ACCESS_TOKEN.equals(typeKey)) {
            byte[] bytes = Decoders.BASE64URL.decode(secretKey);
            return Keys.hmacShaKeyFor(bytes);
        } else if(REFRESH_TOKEN.equals(typeKey)){
            byte[] bytes = Decoders.BASE64URL.decode(refreshKey);
            return Keys.hmacShaKeyFor(bytes);
        } else {
            byte[] bytes = Decoders.BASE64URL.decode(tempKey);
            return Keys.hmacShaKeyFor(bytes);
        }
    }

    public Date getExpiryTime(String token, TypeToken typeKey) {
        Claims claims = getAllClaimsFromToken(token, typeKey);
        Date expiryTime = claims.getExpiration();
        return expiryTime;
    }

//    public JwtInfo parseToken(String token, TypeToken typeKey) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key(typeKey))
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        return JwtInfo.builder()
//                .jwtId(claims.getId())
//                .issuedAt(claims.getIssuedAt())
//                .expiredTime(claims.getExpiration())
//                .build();
//    }
    public Claims getAllClaimsFromToken(String token, TypeToken typeKey) {
        return Jwts.parserBuilder()
                .setSigningKey(key(typeKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isTokenValid(String token, TypeToken typeKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key(typeKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    public String extractUsername(String token, TypeToken typeKey) {
        Claims claims = getAllClaimsFromToken(token, typeKey);
        return claims.getSubject();
    }

    public boolean validateToken(String token, TypeToken typeKey)
        {
            try {
//                if (redisRepository.existsById(parseToken(token, typeKey).getJwtId())) {
//                    return false;
//                } else {
                Jwts.parserBuilder()
                        .setSigningKey(key(typeKey)) //// Lấy key từ phương thức key()
                        .build()
                        //Giải mã token và kiểm tra các thông tin như hết hạn (expiration), tính hợp lệ của cấu trúc.
                        .parse(token);//🔹 Giải mã token. Có thể giải mã cả JWS (signed JWT) lẫn JWT không có chữ ký (unsigned JWT).
                return true;
//                }
            } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
                throw new RuntimeException(e); // 🔥 Token không hợp lệ
            }
        }
        // Tức là JJWT tự kiểm tra hết hạn và các lỗi bảo mật, bạn không cần kiểm tra thủ công nữa.
        ////hết hạn → ném ExpiredJwtException
        ////
        ////sai chữ ký → ném SignatureException
        ////
        ////token lỗi định dạng → MalformedJwtException
        ////
        ////null hoặc không parse được → IllegalArgumentException


}
