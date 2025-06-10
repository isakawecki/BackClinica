package clinica.integrador.service;

import java.sql.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import clinica.integrador.dto.LoginDTO;
import clinica.integrador.entities.Paciente;
import clinica.integrador.repository.PacienteRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

	@Autowired
    private PacienteRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SecretKey jwtSecretKey = Keys.hmacShaKeyFor("uma-chave-bem-segura-de-32bytes!".getBytes());

    private final long jwtExpirationMs = 3600000; // 1 hora

    public String autenticarEGerarToken(LoginDTO login) {
        Optional<Paciente> usuarioOpt = usuarioRepository.findByEmail(login.getEmail());

        if (usuarioOpt.isPresent()) {
            Paciente usuario = usuarioOpt.get();
            if (passwordEncoder.matches(login.getPassword(), usuario.getSenha())) {
                return Jwts.builder()
                        .setSubject(usuario.getEmail())
                        .setIssuedAt(new Date(jwtExpirationMs))
                        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                        .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                        .compact();
            }
        }
        return null; 
    }}