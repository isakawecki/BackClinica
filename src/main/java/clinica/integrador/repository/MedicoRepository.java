package clinica.integrador.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clinica.integrador.entities.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByEmail(String email);

    List<Medico> findByNomeContainingIgnoreCase(String nome);

	Optional<Medico> findByEmailAndSenha(String email, String senha);

}