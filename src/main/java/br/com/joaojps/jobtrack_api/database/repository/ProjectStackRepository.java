package br.com.joaojps.jobtrack_api.database.repository;

import br.com.joaojps.jobtrack_api.database.model.ProjectStack;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectStackRepository extends JpaRepository<ProjectStack, Long> {
    // Busca só as stacks do projeto informado.
    @Query("SELECT stack FROM ProjectStack stack WHERE stack.projectId = :identificadorProjeto")
    List<ProjectStack> buscarTodasPorProjetoId(@Param("identificadorProjeto") Long identificadorProjeto);

    // Confere se a stack pertence ao projeto informado.
    @Query("SELECT stack FROM ProjectStack stack WHERE stack.id = :identificadorStack AND stack.projectId = :identificadorProjeto")
    Optional<ProjectStack> buscarPorIdentificadorEProjeto(
            @Param("identificadorStack") Long identificadorStack,
            @Param("identificadorProjeto") Long identificadorProjeto);
}
