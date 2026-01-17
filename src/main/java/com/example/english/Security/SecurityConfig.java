package com.example.english.Security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Configuration
@EnableWebSecurity // Bật tính năng bảo mật HTTP cho ứng dụng
@EnableMethodSecurity // Cho phép sử dụng @PreAuthorize, @PostAuthorize
public class SecurityConfig {
    UserDetailsService customUserDetailsService;
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    JwtAuthenticationFilter jwtAuthenticationFilter;
    String [] PUBLIC_ENDPOINT={"/users/**","/auth/verify-user","/auth/sign-up","/auth/resend-verification",
            "/auth/sign-in","/auth/forgot-password","/auth/reset-password","/chatbot/chat"};
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        // Từ Spring Security 6.3 trở lên, class DaoAuthenticationProvider không còn method setUserDetailsService() nữa.
        // Nó đã bị xoá và chuyển sang dùng constructor injection thay vì setter injection.
       DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
       daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
       return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(o->o.requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINT).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINT).permitAll()
                .requestMatchers("/swagger-ui/**",
                        "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, PUBLIC_ENDPOINT).permitAll()
                .requestMatchers(HttpMethod.PUT, PUBLIC_ENDPOINT).permitAll()
                .anyRequest().authenticated());
//        httpSecurity.oauth2Client(Customizer.withDefaults());
        httpSecurity.cors(Customizer.withDefaults()); //Quan trong (Bật cors)
        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        return httpSecurity.build();
    }

}
