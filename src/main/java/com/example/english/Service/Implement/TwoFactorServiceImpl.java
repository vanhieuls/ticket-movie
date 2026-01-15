package com.example.english.Service.Implement;

import com.example.english.Service.Interface.TwoFactorService;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TwoFactorServiceImpl implements TwoFactorService {
    SecretGenerator secretGenerator = new DefaultSecretGenerator();
    QrGenerator qrGenerator = new ZxingPngQrGenerator();
    CodeGenerator codeGenerator = new DefaultCodeGenerator();
    TimeProvider timeProvider = new SystemTimeProvider();
    CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    @Override
    public String generateNewSecret() {
       return secretGenerator.generate();
    }

    @Override
    public String generateQrCodeImage(String secret, String username) {
        QrData data = new QrData.Builder()
                .label(username)
                .secret(secret)
                .issuer("movie-booking") // Tên ứng dụng, có thể tùy chỉnh
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6) // Độ dài mã OTP (6)
                .period(30) // Thời gian sống của mã (30 giây)
                .build();
        try {
            byte[] qrCodeBytes = qrGenerator.generate(data);
            return Base64.getEncoder().encodeToString(qrCodeBytes);
        } catch (QrGenerationException e) {
            log.error("Error generating QR code for user {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
    @Override
    public boolean verifyCode(String secret, String code) {
        return verifier.isValidCode(secret, code);
    }
}
