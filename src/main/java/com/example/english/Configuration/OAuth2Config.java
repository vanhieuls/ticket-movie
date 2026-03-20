package com.example.english.Configuration;

import com.example.english.Entity.Role;
import com.example.english.Entity.User;
import com.example.english.Enum.StatusAcc;
import com.example.english.Repository.RoleRepository;
import com.example.english.Repository.UserRepository;
import com.example.english.Security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2Config implements AuthenticationSuccessHandler {
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;
    RoleRepository roleRepository;

    @NonFinal
    @Value("${oauth2.redirect-url:http://localhost:3000/oauth2/redirect}")
    String redirectUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        log.info("OAuth2 login successful for email: {}", email);
        log.debug("OAuth2 attributes: {}", oAuth2User.getAttributes());

        String givenName = oAuth2User.getAttribute("given_name");
        String familyName = oAuth2User.getAttribute("family_name");
        String picture = oAuth2User.getAttribute("picture");

        // Find or create user
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;

        if (userOpt.isEmpty()) {
            log.info("Creating new user from OAuth2 login: {}", email);

            // Find or create USER role
            Role userRole = roleRepository.findById("USER")
                    .orElseGet(() -> {
                        log.warn("USER role not found in database, creating it");
                        Role newRole = Role.builder()
                                .name("USER")
                                .description("Default user role")
                                .build();
                        return roleRepository.save(newRole);
                    });

            // Create new user
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            user = User.builder()
                    .email(email)
                    .firstName(givenName)
                    .lastName(familyName)
                    .avatar(picture)
                    .username(givenName != null ? givenName : email.split("@")[0])
                    .status(StatusAcc.ACTIVE)
                    .nonLocked(true)
                    .twoFactorEnabled(false)
                    .roles(roles)
                    .build();

            user = userRepository.save(user);
            log.info("New user created with ID: {}", user.getId());
        } else {
            user = userOpt.get();
            log.info("Existing user found: {}", user.getUsername());
        }

        // Generate JWT tokens
        String accessToken = jwtTokenProvider.generateToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        log.info("JWT tokens generated for user: {}", user.getUsername());

        // Set tokens in HTTP-only cookies (more secure than query params)
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);  // Prevent XSS attacks
        accessTokenCookie.setSecure(false);   // Set to true in production with HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(24 * 60 * 60); // 24 hours

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);  // Set to true in production with HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        log.info("Tokens set in HTTP-only cookies, redirecting to: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}