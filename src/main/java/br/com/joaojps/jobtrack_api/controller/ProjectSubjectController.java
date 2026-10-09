package br.com.joaojps.jobtrack_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import br.com.joaojps.jobtrack_api.dto.request.SubjectRequest;
import br.com.joaojps.jobtrack_api.dto.response.SubjectResponse;
import br.com.joaojps.jobtrack_api.service.ProjectSubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/subjects")
@Tag(name = "Matérias", description = "Matérias e assuntos estudados em cada projeto.")
// Recebe os pedidos de matérias de um projeto.
public class ProjectSubjectController {

    private final ProjectSubjectService servicoDeMaterias;

    public ProjectSubjectController(ProjectSubjectService servicoDeMaterias) {
        // Guarda o service que cuida das matérias.
        this.servicoDeMaterias = servicoDeMaterias;
    }

    @GetMapping
    @Operation(summary = "Listar matérias", description = "Lista as matérias cadastradas para um projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matérias retornadas."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<List<SubjectResponse>> listarMateriasDoProjeto(@PathVariable("projectId") Long identificadorProjeto) {
        // Lista as matérias ou retorna 404 se o projeto não existir.
        return ResponseEntity.of(servicoDeMaterias.listarMateriasDoProjeto(identificadorProjeto));
    }

    @PostMapping
    @Operation(summary = "Adicionar matéria", description = "Cadastra uma matéria e uma descrição opcional no projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Matéria criada."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<SubjectResponse> adicionarMateriaAoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                    @RequestBody SubjectRequest dadosDaMateria) {
        // Cria a matéria no projeto e informa onde ela pode ser acessada.
        return servicoDeMaterias.adicionarMateriaAoProjeto(identificadorProjeto, dadosDaMateria)
                .map(materiaAdicionada -> ResponseEntity.created(URI.create(
                        "/api/projects/" + identificadorProjeto + "/subjects/" + materiaAdicionada.identificador())).body(materiaAdicionada))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{subjectId}")
    @Operation(summary = "Atualizar matéria", description = "Atualiza o nome e a descrição de uma matéria do projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matéria atualizada."),
            @ApiResponse(responseCode = "404", description = "Projeto ou matéria não encontrado.")
    })
    public ResponseEntity<SubjectResponse> atualizarMateriaDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                    @PathVariable("subjectId") Long identificadorMateria,
                                                                    @RequestBody SubjectRequest dadosDaMateria) {
        // Atualiza a matéria se ela for desse projeto.
        return ResponseEntity.of(servicoDeMaterias.atualizarMateriaDoProjeto(
                identificadorProjeto, identificadorMateria, dadosDaMateria));
    }

    @DeleteMapping("/{subjectId}")
    @Operation(summary = "Remover matéria", description = "Remove uma matéria que pertence ao projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Matéria removida."),
            @ApiResponse(responseCode = "404", description = "Matéria não encontrada nesse projeto.")
    })
    public ResponseEntity<Void> removerMateriaDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                       @PathVariable("subjectId") Long identificadorMateria) {
        // Remove a matéria se ela for desse projeto; se não, retorna 404.
        return servicoDeMaterias.removerMateriaDoProjeto(identificadorProjeto, identificadorMateria)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
