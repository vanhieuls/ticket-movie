package com.example.english.Service.Interface;

public interface TwoFactorService {
    String generateNewSecret();
    String generateQrCodeImage(String secret, String username);
    boolean verifyCode(String secret, String code);
}
