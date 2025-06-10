package clinica.integrador.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinica.integrador.entities.Medico;
import clinica.integrador.entities.Paciente;
import clinica.integrador.repository.MedicoRepository;
import clinica.integrador.service.PacienteService;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteService pacienteService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        // Tenta logar como médico
        Optional<Medico> medicoOpt = medicoRepository.findByEmail(login.getEmail());
        if (medicoOpt.isPresent()) {
            Medico medico = medicoOpt.get();
            if (encoder.matches(login.getSenha(), medico.getSenha())) {
                medico.setSenha(null);
                return ResponseEntity.ok(new LoginResponse("medico", medico));
            }
        }

        // Tenta logar como paciente
        Paciente paciente = pacienteService.buscarPorEmail(login.getEmail());
        if (paciente != null && pacienteService.verificarSenha(login.getSenha(), paciente.getSenha())) {
            paciente.setSenha(null);
            return ResponseEntity.ok(new LoginResponse("paciente", paciente));
        }

        return ResponseEntity.status(401).body("Email ou senha inválidos");
    }

    // DTOs internos
    static class LoginRequest {
        private String email;
        private String senha;

        // getters e setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    static class LoginResponse {
        private String tipo;
        private Object usuario;

        public LoginResponse(String tipo, Object usuario) {
            this.tipo = tipo;
            this.usuario = usuario;
        }

        public String getTipo() { return tipo; }
        public Object getUsuario() { return usuario; }
    }
}
