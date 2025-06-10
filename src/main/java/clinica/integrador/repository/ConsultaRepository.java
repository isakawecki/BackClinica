package clinica.integrador.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import clinica.integrador.entities.Consulta;
import clinica.integrador.entities.Medico;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByMedico_Id(Long medicoId);

    List<Consulta> findByPaciente_Id(Long pacienteId);

    List<Consulta> findByMedico_NomeContainingIgnoreCase(String nome);

    List<Consulta> findByMedico_EspecialidadeContainingIgnoreCase(String especialidade);

    List<Consulta> findByDataHora(LocalDateTime dataHora);

    List<Consulta> findByMedico_NomeContainingIgnoreCaseAndDataHora(String nome, LocalDateTime dataHora);

    List<Consulta> findByMedico_EspecialidadeContainingIgnoreCaseAndDataHora(String especialidade, LocalDateTime dataHora);

    // Verifica conflito de horário para profissional
    boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);

    boolean existsByMedicoAndDataHoraAndIdNot(Medico medico, LocalDateTime dataHora, Long id);

	List<Consulta> findByMedicoId(Long medicoId);
}