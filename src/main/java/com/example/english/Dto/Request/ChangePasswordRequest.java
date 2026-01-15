package com.example.english.Dto.Request;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmPassword
) { }
