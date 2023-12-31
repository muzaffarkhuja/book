package com.example.book.security;

import com.example.book.model.AuthUser;
import com.example.book.model.UserSession;
import com.example.book.repository.UserSessionRepository;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtService {

    @Value("${spring.security.secret.key}")
    private String secretKey;
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private Gson gson;

    public String generateToken(AuthUser authUser){
        String uuid = UUID.randomUUID().toString();
        userSessionRepository.save(new UserSession(uuid, gson.toJson(authUser)));

        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setSubject(uuid)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isExpired(String token){
        return getClaims(token).getExpiration().getTime() < System.currentTimeMillis();
    }

    public AuthUser getSubject(String token){
        String uuid = getClaims(token).getSubject();
        return userSessionRepository.findById(uuid).map(s -> gson.fromJson(s.getUserInfo(), AuthUser.class))
                .orElseThrow(() -> new JwtException("Token is expired"));
    }
}
