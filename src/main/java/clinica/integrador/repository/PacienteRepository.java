package clinica.integrador.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import clinica.integrador.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByEmail(String email);


    List<Paciente> findByNomeContainingIgnoreCase(String nome);

	Optional<Paciente> findByEmailAndSenha(String email, String senha);
}