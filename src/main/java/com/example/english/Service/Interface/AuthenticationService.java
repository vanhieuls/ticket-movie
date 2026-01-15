package com.example.english.Service.Interface;

import com.example.english.Dto.Request.*;
import com.example.english.Dto.Response.TokenResponse;
import com.example.english.Dto.Response.UserResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    //    AuthenticationDto login (LoginRequest request);
//    void logout (String request);
    UserResponse signUp(UserRequest request);
    UserResponse verifyUser(VerifyUserRequest userDto);
    void reSendVerificationEmail(String email);
    void changePassword(ChangePasswordRequest request);
    void forgotPassword(String email) throws MessagingException;
    void resetPassword (ResetPassword resetPassword);
    TokenResponse login(LoginRequest request);
//    AuthenticationDto refreshToken(HttpServletRequest request);
}
