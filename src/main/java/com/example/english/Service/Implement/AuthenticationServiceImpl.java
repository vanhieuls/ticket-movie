package com.example.english.Service.Implement;

import com.example.english.Dto.Request.*;
import com.example.english.Dto.Response.TokenResponse;
import com.example.english.Dto.Response.UserResponse;
import com.example.english.Entity.User;
import com.example.english.Enum.StatusAcc;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.UserMapper;
import com.example.english.Repository.UserRepository;
import com.example.english.Security.JwtTokenProvider;
import com.example.english.Service.Interface.AuthenticationService;
import com.example.english.Service.Interface.EmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    EmailService emailService;
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    PasswordEncoder passwordEncoder;
    @Override
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        if(user.getStatus().equals(StatusAcc.WAITING_CONFIRM)){
            throw new AppException(ErrorCode.ACCOUNT_NOT_VERIFY);
        }
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if(!user.getTwoFactorEnabled()){
                SecurityContextHolder.getContext().setAuthentication(authentication);
                user.setStatus(StatusAcc.ACTIVE);
                userRepository.save(user);
                String accessToken = jwtTokenProvider.generateToken(userDetails);
                String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
                return TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .authenticated(true)
                        .twoFactorRequired(false)
                        .build();
            }
            else{ // Yêu cầu xác thực 2FA
                assert userDetails != null;
                String tempToken = jwtTokenProvider.generateTempToken(userDetails);
                return TokenResponse.builder()
                        .twoFactorRequired(true)
                        .authenticated(false)
                        .tempToken(tempToken)
                        .build();
            }

        }
        catch (LockedException e) {
            // tài khoản bị khóa
            throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        }
        catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
    }


    @Override
    public UserResponse signUp(UserRequest request) {
//        userRepository.existsByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_EMAIL_ALREADY_EXISTS));
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if(userOptional.isPresent() && userOptional.get().getStatus() != StatusAcc.WAITING_CONFIRM){
            throw new AppException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByUsername(request.getUsername())){
            throw  new AppException(ErrorCode.USER_NAME_ALREADY_EXISTS);
        }
        User user = userMapper.toEntity(request);
        user.setStatus(StatusAcc.WAITING_CONFIRM);
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(3));
        user.setTwoFactorEnabled(false);
        user.setNonLocked(true);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        sendVerificationEmail(savedUser);
        return userMapper.toResponse(savedUser);
    }
    private String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // Generate a random 6-digit code
        return String.valueOf(code);
    }
    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "        .header { color: #4a90e2; text-align: center; }\n" +
                "        .code { font-size: 24px; font-weight: bold; text-align: center; margin: 20px 0; padding: 10px; background: #f5f5f5; border-radius: 5px; }\n" +
                "        .button { display: inline-block; padding: 10px 20px; background-color: #4a90e2; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px; }\n" +
                "        .footer { margin-top: 30px; font-size: 12px; color: #999; text-align: center; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <h1>Verify Your Account</h1>\n" +
                "    </div>\n" +
                "    <p>Hello " + user.getUsername() + ",</p>\n" +
                "    <p>Thank you for registering with us. Please use the following verification code to complete your registration:</p>\n" +
                "    \n" +
                "    <div class=\"code\">" + verificationCode + "</div>\n" +
                "    \n" +
                "    <p>If you didn't request this, please ignore this email.</p>\n" +
                "    \n" +
                "    <div class=\"footer\">\n" +
                "        <p>© 2023 Your Company. All rights reserved.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        try{
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        }
        catch (MessagingException e){
            e.printStackTrace();
            //in Dòng lỗi (error message).
            //Stack trace (toàn bộ luồng code dẫn đến lỗi).
            // Giúp bạn nhanh chóng phát hiện nguyên nhân lỗi khi phát triển ứng dụng.
        }
    }

    @Override
    public UserResponse verifyUser(VerifyUserRequest userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        if (!user.getVerificationCode().equals(userDto.getVerificationCode())) {
            throw new AppException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        user.setStatus(StatusAcc.ACTIVE);
        user.setVerificationCode(null);
        user.setVerificationExpiresAt(null);
        User use = userRepository.save(user);
        return userMapper.toResponse(use);
    }

    @Override
    public void reSendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        if(user.getStatus() != StatusAcc.WAITING_CONFIRM){
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
        }
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(3));
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        UserDetails userDetails = (UserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert userDetails != null;
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
//        passwordEncoder.matches(rawPassword, encodedPassword);
//        rawPassword: mật khẩu dạng thô, chưa mã hóa (ví dụ: "123456")
//encodedPassword: mật khẩu đã được mã hóa bằng passwordEncoder.encode(...) và lưu trong DB (ví dụ: "$2a$10$J1i7...k")
        if(!passwordEncoder.matches(request.oldPassword(),user.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        if(!request.newPassword().equals(request.confirmPassword())){
            throw new AppException(ErrorCode.INVALID_CONFIRM_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        String resetToken = generateToken();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        // Gửi email với link đặt lại mật khẩu
        String resetLink = "http://localhost:5173/reset-password/?token=" + resetToken;
        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <style>\n" +
                "        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "        .header { color: #4F46E5; text-align: center; margin-bottom: 25px; }\n" +
                "        .logo { font-size: 24px; font-weight: bold; margin-bottom: 10px; }\n" +
                "        .content { background-color: #F9FAFB; padding: 25px; border-radius: 8px; }\n" +
                "        .button { display: inline-block; padding: 12px 24px; background-color: #4F46E5; color: white !important; text-decoration: none; border-radius: 6px; font-weight: bold; margin: 20px 0; }\n" +
                "        .footer { margin-top: 30px; font-size: 12px; color: #9CA3AF; text-align: center; }\n" +
                "        .code { font-family: monospace; background-color: #E5E7EB; padding: 8px 12px; border-radius: 4px; }\n" +
                "        .divider { height: 1px; background-color: #E5E7EB; margin: 20px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <div class=\"logo\">YourApp</div>\n" +
                "        <h2>Reset Your Password</h2>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div class=\"content\">\n" +
                "        <p>Hello " + user.getUsername() + ",</p>\n" +
                "        <p>We received a request to reset your password. Click the button below to proceed:</p>\n" +
                "        \n" +
                "        <div style=\"text-align: center;\">\n" +
                "            <a href=\"" + resetLink + "\" class=\"button\">Reset Password</a>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"divider\"></div>\n" +
                "        \n" +
                "        <p>If you didn't request this, you can safely ignore this email. This link will expire in <strong>15 minutes</strong>.</p>\n" +
                "        \n" +
                "        <p>Or copy this link manually:</p>\n" +
                "        <p class=\"code\">" + resetLink + "</p>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div class=\"footer\">\n" +
                "        <p>© 2023 YourApp. All rights reserved.</p>\n" +
                "        <p>If you have any questions, contact us at support@yourapp.com</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        try{
            emailService.sendVerificationEmail(email, "Reset Your Password", htmlMessage);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    @Override
    public void resetPassword(ResetPassword resetPassword) {
        User user = userRepository.findByResetPasswordToken(resetPassword.token()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getResetPasswordExpiry().isBefore(LocalDateTime.now())) {
            user.setResetPasswordToken(null);
            user.setResetPasswordExpiry(null);
            userRepository.save(user);
            throw new AppException(ErrorCode.RESET_TOKEN_EXPIRED);
        }
        user.setPassword(passwordEncoder.encode(resetPassword.newPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        userRepository.save(user);
    }
}
