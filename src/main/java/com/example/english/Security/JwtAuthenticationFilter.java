package com.example.english.Security;

import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.english.Enum.TypeToken.ACCESS_TOKEN;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    UserDetailsService userDetailsService;
    JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token =getTokenFromHeader(request);
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token, ACCESS_TOKEN)){
            String username =jwtTokenProvider.extractUsername(token, ACCESS_TOKEN);
            //load user from db
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(!userDetails.isAccountNonLocked()){
                throw new AppException(ErrorCode.ACCOUNT_BE_LOCKED);
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        // Không gọi filterChain.doFilter(): Request sẽ bị chặn, không bao giờ tới được controller.
        // Cho request đi tiếp đến filter/controller tiếp theo
        filterChain.doFilter(request,response);
    }
    private String getTokenFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
