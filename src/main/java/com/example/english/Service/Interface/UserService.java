package com.example.english.Service.Interface;

import com.example.english.Dto.Request.UserRequest;
import com.example.english.Dto.Request.VerifyCodeRequest;
import com.example.english.Dto.Response.TokenResponse;
import com.example.english.Dto.Response.TwoFactorResponse;
import com.example.english.Dto.Response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    void deleteUser(Long id);
    UserResponse updateUser(UserRequest userRequest);
    UserResponse getUserById(Long id);
    UserResponse getUserInfo();
    List<UserResponse> getAllUsers();
    TwoFactorResponse enableTwoFactor();
    TokenResponse verifyCode(String tempToken, VerifyCodeRequest verifyCodeRequest);
}
