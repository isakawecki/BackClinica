package clinica.integrador.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    private static final String SECRET = "12345678901234567890123456789012"; // 32+ chars obrigatórios
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Gera o token com o subject (ex: email ou ID)
    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrai o subject (e-mail ou ID) do token
    public String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // Extrai o ID (Long) do Authorization Header, se o subject for um número
    public Long extrairIdDoToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) return null;

        String token = authorizationHeader.replace("Bearer ", "");
        String subject = getSubjectFromToken(token);

        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
