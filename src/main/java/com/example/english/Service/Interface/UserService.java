package com.example.english.Service.Interface;

import com.example.english.Dto.Request.UserRequest;
import com.example.english.Dto.Request.UserUpdateRequest;
import com.example.english.Dto.Request.VerifyCodeRequest;
import com.example.english.Dto.Response.TokenResponse;
import com.example.english.Dto.Response.TwoFactorResponse;
import com.example.english.Dto.Response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    void deleteUser(Long id);
    UserResponse updateUser(UserUpdateRequest userRequest);
    UserResponse getUserById(Long id);
    UserResponse getUserInfo();
    Page<UserResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String username, String fullName, String email);
    TwoFactorResponse enableTwoFactor();
    TokenResponse verifyCode(String tempToken, VerifyCodeRequest verifyCodeRequest);
    UserResponse assignRoleToUser(Long userId, String roleName);
    UserResponse removeRoleFromUser(Long userId, String roleName);
}
