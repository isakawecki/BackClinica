package clinica.integrador.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import clinica.integrador.dto.ConsultaDTO;
import clinica.integrador.entities.Consulta;
import clinica.integrador.entities.Medico;
import clinica.integrador.entities.Paciente;
import clinica.integrador.repository.ConsultaRepository;
import clinica.integrador.repository.MedicoRepository;
import clinica.integrador.repository.PacienteRepository;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll().stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<ConsultaDTO> buscarPorId(Long id) {
        return consultaRepository.findById(id).map(ConsultaDTO::new);
    }

    public Consulta salvar(ConsultaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Medico não encontrado"));

        boolean existeConflito = consultaRepository.existsByMedicoAndDataHora(medico, dto.getDataHora());
        if (existeConflito) {
            throw new RuntimeException("Já existe uma consulta marcada para esse profissional neste horário.");
        }

        Consulta nova = new Consulta();
        nova.setPaciente(paciente);
        nova.setMedico(medico);
        nova.setDataHora(dto.getDataHora());
        nova.setObservacoes(dto.getObservacoes());
        nova.setTelefonePaciente(dto.getTelefonePaciente());
        nova.setProcedimento(dto.getProcedimento());

        return consultaRepository.save(nova);
    }

    public Consulta atualizar(Long id, ConsultaDTO dto) {
        Consulta existente = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
        	    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Medico não encontrado"));

        boolean conflito = consultaRepository.existsByMedicoAndDataHoraAndIdNot(medico, dto.getDataHora(), id);
        if (conflito) {
            throw new RuntimeException("Já existe uma consulta marcada para esse medico neste horário.");
        }

        existente.setPaciente(paciente);
        existente.setMedico(medico);
        existente.setDataHora(dto.getDataHora());
        existente.setObservacoes(dto.getObservacoes());
        existente.setTelefonePaciente(dto.getTelefonePaciente());
        existente.setProcedimento(dto.getProcedimento());

        return consultaRepository.save(existente);
    }

    public boolean deletar(Long id) {
        if (!consultaRepository.existsById(id)) {
            return false;
        }
        consultaRepository.deleteById(id);
        return true;
    }

    public List<ConsultaDTO> buscarPorMedico(String nome) {
        return consultaRepository.findByMedico_NomeContainingIgnoreCase(nome).stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }

    public List<ConsultaDTO> buscarPorEspecialidade(String especialidade) {
        return consultaRepository.findByMedico_EspecialidadeContainingIgnoreCase(especialidade).stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }

    public List<ConsultaDTO> buscarPorDataHora(LocalDateTime dataHora) {
        return consultaRepository.findByDataHora(dataHora).stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }

    public List<ConsultaDTO> buscarPorMedicoEData(String nome, LocalDateTime dataHora) {
        return consultaRepository.findByMedico_NomeContainingIgnoreCaseAndDataHora(nome, dataHora).stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }

    public List<ConsultaDTO> buscarPorEspecialidadeEData(String especialidade, LocalDateTime dataHora) {
        return consultaRepository.findByMedico_EspecialidadeContainingIgnoreCaseAndDataHora(especialidade, dataHora).stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }

    public List<ConsultaDTO> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPaciente_Id(pacienteId).stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }
    public List<ConsultaDTO> listarPorMedico(Long medicoId) {
        List<Consulta> consultas = consultaRepository.findByMedico_Id(medicoId);
        return consultas.stream()
                .map(ConsultaDTO::new)
                .collect(Collectors.toList());
    }

}