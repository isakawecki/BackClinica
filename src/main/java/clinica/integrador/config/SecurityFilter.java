package clinica.integrador.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import clinica.integrador.entities.Medico;
import clinica.integrador.entities.Paciente;
import clinica.integrador.repository.MedicoRepository;
import clinica.integrador.repository.PacienteRepository;
import clinica.integrador.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);
        if (token != null) {
            String email = tokenService.getSubjectFromToken(token); 

           
            Optional<Paciente> optionalPaciente = pacienteRepository.findByEmail(email);

            if (optionalPaciente.isPresent()) {
                Paciente paciente = optionalPaciente.get();

                List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + paciente.getRole().name())
                );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(paciente, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } else {
               
                Optional<Medico> optionalMedico = medicoRepository.findByEmail(email);

                if (optionalMedico.isPresent()) {
                    Medico medico = optionalMedico.get();

                    List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + medico.getRole().name())
                    );

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(medico, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7);
    }

  
}
