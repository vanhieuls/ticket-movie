package com.example.english.Service.Implement;

import com.example.english.Dto.Request.UserRequest;
import com.example.english.Dto.Request.VerifyCodeRequest;
import com.example.english.Dto.Response.TokenResponse;
import com.example.english.Dto.Response.TwoFactorResponse;
import com.example.english.Dto.Response.UserResponse;
import com.example.english.Entity.User;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.UserMapper;
import com.example.english.Repository.UserRepository;
import com.example.english.Security.JwtTokenProvider;
import com.example.english.Service.Interface.TwoFactorService;
import com.example.english.Service.Interface.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.english.Enum.TypeToken.TEMP_TOKEN;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    UserRepository userRepository;
    TwoFactorService twoFactorService;
    JwtTokenProvider jwtTokenProvider;
    UserMapper userMapper;

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public UserResponse updateUser(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserResponse getUserById(Long id) {
        return null;
    }

    @Override
    public UserResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        UserDetails user = (UserDetails) authentication.getPrincipal();
        assert user != null;
        String username = user.getUsername();
        User userEntity = userRepository.findByUsername(username).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(userEntity);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public TwoFactorResponse enableTwoFactor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        UserDetails user = (UserDetails) authentication.getPrincipal();
        assert user != null;
        String username = user.getUsername();
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        TwoFactorResponse twoFactorResponse = new TwoFactorResponse();
        if(userEntity.getTwoFactorEnabled()) {
            twoFactorResponse.setSuccess(false);
            userEntity.setTwoFactorEnabled(false);
            userEntity.setTwoFactorSecret(null);
            userRepository.save(userEntity);
            return twoFactorResponse;
        }
        String secret =  twoFactorService.generateNewSecret();
        String qrCode = twoFactorService.generateQrCodeImage(secret, username);
        twoFactorResponse.setSuccess(true);
        twoFactorResponse.setQrCode(qrCode);
        userEntity.setTwoFactorSecret(secret);
        userEntity.setTwoFactorEnabled(true);
        userRepository.save(userEntity);
        return twoFactorResponse;
    }

    @Override
    public TokenResponse verifyCode(String tempToken, VerifyCodeRequest verifyCodeRequest) {
        log.info("Temp token: {}", tempToken);
        if(tempToken == null || tempToken.isBlank()) {
            throw new RuntimeException("Temp token is missing");
        }
        if(!jwtTokenProvider.validateToken(tempToken, TEMP_TOKEN)){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        String username = jwtTokenProvider.extractUsername(tempToken, TEMP_TOKEN);
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        if(twoFactorService.verifyCode(userEntity.getTwoFactorSecret(), verifyCodeRequest.code())) {
            String accessToken = jwtTokenProvider.generateToken(userEntity);
            String refreshToken = jwtTokenProvider.generateRefreshToken(userEntity);
            return TokenResponse.builder()
                    .tempToken(null)
                    .twoFactorRequired(false)
                    .authenticated(true)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        else {
            throw new AppException(ErrorCode.INVALID_2FA_CODE);
        }
    }
}
