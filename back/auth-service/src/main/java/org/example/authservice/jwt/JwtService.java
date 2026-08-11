package org.example.authservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration;

    private final RedisTemplate<String, Object> redisTemplate;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String login, String userName) {
        return generateToken(login, userName, accessTokenExpiration);
    }

    public String generateRefreshToken(String login, String userName) {
        return generateToken(login, userName, refreshTokenExpiration);
    }

    private String generateToken(String login, String userName, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(login)
                .claim("userName", userName)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractLogin(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractUserName(String token) {
        return extractClaims(token).get("userName", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                log.warn("Token is blacklisted");
                return false;
            }

            Claims claims = extractClaims(token);
            boolean isValid = !claims.getExpiration().before(new Date());

            if (!isValid) {
                log.debug("Token has expired");
            }

            return isValid;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public void blacklistToken(String token) {
        try {
            Claims claims = extractClaims(token);
            long expiration = claims.getExpiration().getTime() - System.currentTimeMillis();

            if (expiration > 0) {
                String key = "blacklist:" + token;
                redisTemplate.opsForValue().set(key, "blacklisted", expiration, TimeUnit.MILLISECONDS);
                log.info("Token blacklisted successfully. Expires in: {} ms", expiration);
            } else {
                log.warn("Token already expired, no need to blacklist");
            }
        } catch (Exception e) {
            log.error("Failed to blacklist token: {}", e.getMessage());
        }
    }

    public boolean isTokenBlacklisted(String token) {
        String key = "blacklist:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void saveRefreshToken(String login, String refreshToken) {
        String key = "refresh:" + login;
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenExpiration, TimeUnit.MILLISECONDS);
        log.info("Refresh token saved for user: {}", login);
    }

    public String getRefreshToken(String login) {
        String key = "refresh:" + login;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String login) {
        String key = "refresh:" + login;
        redisTemplate.delete(key);
        log.info("Refresh token deleted for user: {}", login);
    }

    public boolean validateRefreshToken(String login, String refreshToken) {
        String savedToken = getRefreshToken(login);
        boolean isValid = savedToken != null && savedToken.equals(refreshToken) && isTokenValid(refreshToken);

        if (!isValid) {
            log.warn("Invalid refresh token for user: {}", login);
        }

        return isValid;
    }
}