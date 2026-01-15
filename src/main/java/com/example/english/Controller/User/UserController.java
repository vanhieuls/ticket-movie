package com.example.english.Controller.User;

import com.example.english.Dto.Request.VerifyCodeRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.TokenResponse;
import com.example.english.Dto.Response.TwoFactorResponse;
import com.example.english.Dto.Response.UserResponse;
import com.example.english.Service.Interface.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Controller", description = "APIs cho nguời dùng kèm tính năng bật và xác thực 2FA")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @Operation(
            summary = "Bật/Tắt xác thực hai yếu tố (2FA) cho người dùng hiện tại"
    )
    @PostMapping("/2fa/enable")
    public ApiResponse<TwoFactorResponse> enableTwoFactor() {
        TwoFactorResponse response = userService.enableTwoFactor();
        return ApiResponse.<TwoFactorResponse>builder()
                .code(200)
                .message("2FA enabled")
                .result(response)
                .build();
    }

    @Operation(
            summary = "Xác minh mã 2FA và nhận token truy cập (nếu thành công)"
    )
    @PostMapping("/2fa/verify")
    public ApiResponse<TokenResponse> verifyCode(@RequestHeader("TempToken") String request, @RequestBody VerifyCodeRequest verifyCodeRequest) {
        TokenResponse tokenResponse = userService.verifyCode(request, verifyCodeRequest);
        return ApiResponse.<TokenResponse>builder()
                .code(200)
                .message("2FA verified")
                .result(tokenResponse)
                .build();
    }

    @Operation(
            summary = "Lấy thông tin người dùng hiện tại"
    )
    @GetMapping("/infor")
    public ApiResponse<UserResponse> getUserInfo() {
        UserResponse userResponse = userService.getUserInfo();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("User info retrieved")
                .result(userResponse)
                .build();
    }
}
