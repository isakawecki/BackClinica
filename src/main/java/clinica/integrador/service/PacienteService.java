package clinica.integrador.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import clinica.integrador.entities.Paciente;
import clinica.integrador.repository.PacienteRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String JWT_SECRET = "minhaChaveSecretaMuitoSegura1234567890"; // mínimo 32 chars

    public Paciente salvar(Paciente paciente) {
        String senhaCriptografada = passwordEncoder.encode(paciente.getSenha());
        paciente.setSenha(senhaCriptografada);
        return pacienteRepository.save(paciente);
    }

    public Paciente buscarPorEmail(String email) {
        return pacienteRepository.findByEmail(email).orElse(null);
    }

    public String autenticar(String email, String senha) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByEmail(email);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            if (passwordEncoder.matches(senha, paciente.getSenha())) {
                SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

                return Jwts.builder()
                        .setSubject(email)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                        .signWith(key, SignatureAlgorithm.HS256)
                        .compact();
            } else {
                throw new RuntimeException("Senha incorreta");
            }
        }
        throw new RuntimeException("Paciente não encontrado");
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    public Optional<Paciente> atualizar(Long id, Paciente pacienteAtualizado) {
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setNome(pacienteAtualizado.getNome());
            paciente.setEmail(pacienteAtualizado.getEmail());
            paciente.setTelefone(pacienteAtualizado.getTelefone());
            return pacienteRepository.save(paciente);
        });
    }

    public boolean deletar(Long id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
