package br.com.joaojps.jobtrack_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "JobTrack API",
        version = "1.0.0",
        description = "API para acompanhar projetos de desenvolvimento, matérias estudadas e tecnologias usadas."
))
@SpringBootApplication
// Inicia o Spring Boot e prepara a API.
public class JobtrackApiApplication {

	public static void main(String[] args) {
		// Pede ao Spring que configure e inicie a aplicação.
		SpringApplication.run(JobtrackApiApplication.class, args);
	}

}
