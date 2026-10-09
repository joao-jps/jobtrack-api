package br.com.joaojps.jobtrack_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import br.com.joaojps.jobtrack_api.database.model.ProjectStatus;
import br.com.joaojps.jobtrack_api.dto.request.ProjectRequest;
import br.com.joaojps.jobtrack_api.dto.request.StatusRequest;
import br.com.joaojps.jobtrack_api.dto.response.ProjectResponse;
import br.com.joaojps.jobtrack_api.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projetos", description = "Criação, consulta e acompanhamento do status dos projetos.")
// Recebe os pedidos de projeto e chama o service para cuidar deles.
public class ProjectController {

    private final ProjectService servicoDeProjetos;

    public ProjectController(ProjectService servicoDeProjetos) {
        // Guarda o service que cuida das regras dos projetos.
        this.servicoDeProjetos = servicoDeProjetos;
    }

    @GetMapping
    @Operation(summary = "Listar projetos", description = "Retorna os projetos cadastrados com suas matérias e stacks.")
    public List<ProjectResponse> listarProjetos() {
        // Pede a lista ao service e devolve o resultado.
        return servicoDeProjetos.listarProjetos();
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Buscar projeto", description = "Busca um projeto pelo ID, incluindo suas matérias e stacks.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto encontrado."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<ProjectResponse> buscarProjetoPorIdentificador(@PathVariable("projectId") Long identificadorProjeto) {
        // Devolve o projeto encontrado ou 404 se ele não existir.
        return ResponseEntity.of(servicoDeProjetos.buscarProjetoPorIdentificador(identificadorProjeto));
    }

    @PostMapping
    @Operation(summary = "Criar projeto", description = "Cadastra um projeto com nome, objetivo e status.")
    @ApiResponse(responseCode = "201", description = "Projeto criado.")
    public ResponseEntity<ProjectResponse> adicionarProjeto(@RequestBody ProjectRequest dadosDoProjeto) {
        // Cria o projeto e informa onde ele pode ser acessado.
        ProjectResponse projetoAdicionado = servicoDeProjetos.adicionarProjeto(dadosDoProjeto);
        return ResponseEntity.created(URI.create("/api/projects/" + projetoAdicionado.identificador())).body(projetoAdicionado);
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Atualizar projeto", description = "Troca o nome, o objetivo e o status do projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto atualizado."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<ProjectResponse> atualizarProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                           @RequestBody ProjectRequest dadosDoProjeto) {
        // Atualiza os dados do projeto ou retorna 404 se ele não existir.
        return ResponseEntity.of(servicoDeProjetos.atualizarProjeto(identificadorProjeto, dadosDoProjeto));
    }

    @PatchMapping("/{projectId}/status")
    @Operation(summary = "Atualizar status", description = "Muda apenas o status do projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<ProjectResponse> atualizarStatusProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                 @RequestBody StatusRequest dadosDoStatus) {
        // Muda só o status do projeto.
        ProjectStatus novoStatus = dadosDoStatus.status();
        return ResponseEntity.of(servicoDeProjetos.atualizarStatusProjeto(identificadorProjeto, novoStatus));
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Remover projeto", description = "Remove um projeto pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Projeto removido."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<Void> removerProjeto(@PathVariable("projectId") Long identificadorProjeto) {
        // Remove o projeto; retorna 204 se der certo ou 404 se ele não existir.
        return servicoDeProjetos.removerProjeto(identificadorProjeto)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
