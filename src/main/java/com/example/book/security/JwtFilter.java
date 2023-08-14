package com.example.book.security;

import com.example.book.dto.ResponseDto;
import com.example.book.model.AuthUser;
import com.example.book.service.validator.AppStatusCodes;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtService jwtService;
    @Autowired
    private  Gson gson;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")){
            String token = authorization.substring(7);
            if (jwtService.isExpired(token)){
                response.getWriter().println(gson.toJson(ResponseDto.builder()
                        .code(AppStatusCodes.VALIDATION_ERROR_CODE)
                        .message("Token is expired!")
                        .build()));
                response.setContentType("application/json");
                response.setStatus(400);
            } else {
                AuthUser authUser = jwtService.getSubject(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
