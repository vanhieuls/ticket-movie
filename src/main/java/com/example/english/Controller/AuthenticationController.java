package com.example.english.Controller;

import com.example.english.Dto.Request.*;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.TokenResponse;
import com.example.english.Dto.Response.UserResponse;
import com.example.english.Service.Interface.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "API để xác thực người dùng và quản lý tài khoản")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Operation(
            summary = "Đăng nhập người dùng"
    )
    @PostMapping("/sign-in")
    public ApiResponse<TokenResponse> signIn(@RequestBody LoginRequest request){
        TokenResponse tokenResponse = authenticationService.login(request);
        return ApiResponse.<TokenResponse>builder()
                .result(tokenResponse)
                .message("Success")
                .build();
    }

    @Operation(
            summary = "Đăng ký người dùng mới"
    )
    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> signUp(@RequestBody UserRequest request){
        UserResponse userResponse = authenticationService.signUp(request);
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .message("User registered successfully, please verify your email.")
                .build();
    }

    @Operation(
            summary = "Xác minh người dùng bằng mã xác minh"
    )
    @PostMapping("/verify-user")
    public ApiResponse<UserResponse> verifyUser(@RequestBody VerifyUserRequest userDto){
        UserResponse userResponse = authenticationService.verifyUser(userDto);
        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .message("User verified successfully.")
                .build();
    }

    @Operation(
            summary = "Gửi lại email xác minh"
    )
    @PostMapping("/resend-verification")
    public ApiResponse<Void> reSendVerificationEmail(@RequestParam String email){
        authenticationService.reSendVerificationEmail(email);
        return ApiResponse.<Void>builder()
                .message("Verification email resent successfully.")
                .build();
    }

    @Operation(
            summary = "Thay đổi mật khẩu người dùng"
    )
    @PatchMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request){
        authenticationService.changePassword(request);
        return ApiResponse.<Void>builder()
                .message("Password changed successfully.")
                .build();
    }

    @Operation(
            summary = "Yêu cầu đặt lại mật khẩu (quên mật khẩu)"
    )
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestParam String email){
        try{
            authenticationService.forgotPassword(email);
        } catch (MessagingException e) {
            log.error("Error sending password reset email to {}: {}", email, e.getMessage());
            return ApiResponse.<Void>builder()
                    .message("Failed to send password reset email.")
                    .build();
        }
        return ApiResponse.<Void>builder()
                .message("Password reset email sent successfully.")
                .build();
    }

    @Operation(
            summary = "Đặt lại mật khẩu người dùng"
    )
    @PostMapping("/reset-password")
    public ApiResponse <Void> resetPassword (@RequestBody ResetPassword resetPassword){
        authenticationService.resetPassword(resetPassword);
        return ApiResponse.<Void>builder()
                .message("Password reset successfully.")
                .build();
    }
}
