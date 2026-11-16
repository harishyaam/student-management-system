
package com.example.first;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {  //Reads the Base64 secret and creates a cryptographic key.
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) { //Creates a signed JWT containing the username and expiration.
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) { //Creates a signed JWT containing the username and expiration.
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) { //Checks the username and expiration after verifying the signature.
        Claims claims = extractAllClaims(token);

        return claims.getSubject().equals(username)
                && claims.getExpiration().after(new Date());
    }

    private Claims extractAllClaims(String token) { //Checks the username and expiration after verifying the signature.
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}