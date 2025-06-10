package clinica.integrador.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinica.integrador.entities.Medico;
import clinica.integrador.repository.MedicoRepository;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/cadastrar")
    public ResponseEntity<Medico> cadastrar(@RequestBody Medico medico) {
        medico.setSenha(encoder.encode(medico.getSenha()));
        Medico novoMedico = medicoRepository.save(medico);
        return ResponseEntity.ok(novoMedico);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Medico loginRequest) {
        Optional<Medico> MedicoOpt = medicoRepository.findByEmail(loginRequest.getEmail());

        if (MedicoOpt.isPresent()) {
            Medico medico = MedicoOpt.get();
            if (encoder.matches(loginRequest.getSenha(), medico.getSenha())) {
                medico.setSenha(null); 
                return ResponseEntity.ok(medico);
            }
        }

        return ResponseEntity.status(401).body("Email ou senha inválidos");
    }

    
    @GetMapping("/meu-perfil")
    public ResponseEntity<Medico> getPerfil() {
        Long profissionalId = getProfissionalIdLogado(); 

        return medicoRepository.findById(profissionalId)
                .map(profissional -> {
                    profissional.setSenha(null);
                    return ResponseEntity.ok(profissional);
                })
                .orElse(ResponseEntity.status(404).build());
    }

	private Long getProfissionalIdLogado() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GetMapping("/todos")
	public ResponseEntity<List<Medico>> listarTodos() {
	    List<Medico> lista = medicoRepository.findAll();
	 
	    lista.forEach(profissional -> profissional.setSenha(null));
	    return ResponseEntity.ok(lista);
	}
    
    
}
