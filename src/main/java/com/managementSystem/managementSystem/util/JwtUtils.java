package com.managementSystem.managementSystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private long expiration;

        private Key getSigningKey() {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Add roles as a claim
        claims.put("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // Just username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException e) {
                return false;
            }
        }

        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        }
    }

//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration.ms}")
//    private long jwtExpirationMs;
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .claim("roles", roles)  // ✅ Store roles properly
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS256, getSigningKey())
//                .compact();
//    }
////
////
////    Claims claims = Jwts.parserBuilder()
////            .setSigningKey(getSigningKey())
////            .build()
////            .parseClaimsJws(token)
////            .getBody();
////
////    String username = claims.getSubject();
////    List<String> roles = claims.get("roles", List.class);
////
////    List<GrantedAuthority> authorities = roles.stream()
////            .map(SimpleGrantedAuthority::new)
////            .collect(Collectors.toList());
////
////    UsernamePasswordAuthenticationToken authenticationToken =
////            new UsernamePasswordAuthenticationToken(username, null, authorities);
////SecurityContextHolder.getContext().setAuthentication(authenticationToken);


