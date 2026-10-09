package br.com.joaojps.jobtrack_api.database.repository;

import br.com.joaojps.jobtrack_api.database.model.ProjectSubject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectSubjectRepository extends JpaRepository<ProjectSubject, Long> {
    @Query("SELECT materia FROM ProjectSubject materia WHERE materia.projectId = :identificadorProjeto")
    List<ProjectSubject> buscarTodasPorProjetoId(@Param("identificadorProjeto") Long identificadorProjeto);

    @Query("SELECT materia FROM ProjectSubject materia WHERE materia.id = :identificadorMateria AND materia.projectId = :identificadorProjeto")
    Optional<ProjectSubject> buscarPorIdentificadorEProjeto(
            @Param("identificadorMateria") Long identificadorMateria,
            @Param("identificadorProjeto") Long identificadorProjeto);
}
