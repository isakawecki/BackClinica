package clinica.integrador.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import clinica.integrador.dto.ConsultaDTO;
import clinica.integrador.entities.Consulta;
import clinica.integrador.service.ConsultaService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> buscarPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ConsultaDTO dto) {
        System.out.println("[DEBUG] Salvando consulta com profissionalId = " + dto.getMedicoId() + " e pacienteId = " + dto.getPacienteId());
        try {
            Consulta salvo = consultaService.salvar(dto);
            return ResponseEntity.ok(new ConsultaDTO(salvo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> atualizar(@PathVariable Long id, @RequestBody ConsultaDTO dto) {
        System.out.println("[DEBUG] Atualizando consulta id " + id + " com profissionalId = " + dto.getMedicoId() + " e pacienteId = " + dto.getPacienteId());
        try {
            Consulta atualizada = consultaService.atualizar(id, dto);
            return ResponseEntity.ok(new ConsultaDTO(atualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean ok = consultaService.deletar(id);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/profissional")
    public ResponseEntity<List<ConsultaDTO>> buscarPorMedico(@RequestParam String nome) {
        return ResponseEntity.ok(consultaService.buscarPorMedico(nome));
    }

    @GetMapping("/especialidade")
    public ResponseEntity<List<ConsultaDTO>> buscarPorEspecialidade(@RequestParam String especialidade) {
        return ResponseEntity.ok(consultaService.buscarPorEspecialidade(especialidade));
    }

    @GetMapping("/data")
    public ResponseEntity<List<ConsultaDTO>> buscarPorData(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        return ResponseEntity.ok(consultaService.buscarPorDataHora(dataHora));
    }

    @GetMapping("/profissional-data")
    public ResponseEntity<List<ConsultaDTO>> buscarPorProfissionalEData(
        @RequestParam String nome,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        return ResponseEntity.ok(consultaService.buscarPorMedicoEData(nome, dataHora));
    }

    @GetMapping("/especialidade-data")
    public ResponseEntity<List<ConsultaDTO>> buscarPorEspecialidadeEData(
        @RequestParam String especialidade,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        return ResponseEntity.ok(consultaService.buscarPorEspecialidadeEData(especialidade, dataHora));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/profissional/{id}")
    public ResponseEntity<List<ConsultaDTO>> listarConsultasDoProfissional(@PathVariable Long id) {
        List<ConsultaDTO> consultas = consultaService.listarPorMedico(id);
        if (consultas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(consultas);
    }
    
}
