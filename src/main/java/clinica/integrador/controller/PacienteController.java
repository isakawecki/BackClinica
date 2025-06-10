package clinica.integrador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clinica.integrador.entities.Paciente;
import clinica.integrador.service.PacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;


    @PostMapping("/cadastrar")
    public ResponseEntity<Paciente> cadastrarPaciente(@RequestBody Paciente paciente) {
        Paciente salvo = pacienteService.salvar(paciente);
        return ResponseEntity.ok(salvo);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginPaciente(@RequestBody Paciente paciente) {
        String token = pacienteService.autenticar(paciente.getEmail(), paciente.getSenha());
        
        if (token != null) {
            Paciente pacienteAutenticado = pacienteService.buscarPorEmail(paciente.getEmail());
            return ResponseEntity.ok(pacienteAutenticado); // ou retornar um DTO com os dados essenciais
        } else {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }
    }

    

    }
