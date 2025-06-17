package clinica.integrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "clinica.integrador.entities")
@EnableJpaRepositories(basePackages = "clinica.integrador.repository")
public class IntegradorApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegradorApplication.class, args);
	}
}
