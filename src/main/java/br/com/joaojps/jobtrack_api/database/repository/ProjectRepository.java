package br.com.joaojps.jobtrack_api.database.repository;

import br.com.joaojps.jobtrack_api.database.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

// Busca, salva e remove projetos para os services.
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
