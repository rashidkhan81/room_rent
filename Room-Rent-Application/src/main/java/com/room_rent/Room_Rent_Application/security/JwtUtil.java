package com.room_rent.Room_Rent_Application.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final Key key;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms}") long expirationMs,
                   @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }


    public String generateToken(String email , String role,Long userId, String userName) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .claim("role",role)
                .claim("userId",userId)
                .claim("name",userName)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //here i am doing chage in order to get the refresh token
    public String generateRefreshToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }



    public String getSubject(String token) {
        return parse(token).getBody().getSubject();
    }


    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build().
                parseClaimsJws(token);
    }

    public String validateAndExtractSubject(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
    }




    //redis
    // Parse claims
//    private Claims getClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
    // For Redis Blacklist: get remaining expiration time
//    public long getExpirationMillis(String token) {
//        Date expiration = getClaims(token).getExpiration();
//        return expiration.getTime() - System.currentTimeMillis();
//    }

//to store the abstract all the datas
    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = parse(token).getBody();
        String roles = claims.get("role", String.class); // or "roles" if multiple
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
