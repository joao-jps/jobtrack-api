package br.com.joaojps.jobtrack_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import br.com.joaojps.jobtrack_api.dto.request.StackRequest;
import br.com.joaojps.jobtrack_api.dto.response.StackResponse;
import br.com.joaojps.jobtrack_api.service.ProjectStackService;
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
@RequestMapping("/api/projects/{projectId}/stacks")
@Tag(name = "Stacks", description = "Tecnologias e versões usadas em cada projeto.")
// Recebe os pedidos de stacks de um projeto.
public class ProjectStackController {

    private final ProjectStackService servicoDeStacks;

    public ProjectStackController(ProjectStackService servicoDeStacks) {
        // Guarda o service que cuida das stacks.
        this.servicoDeStacks = servicoDeStacks;
    }

    @GetMapping
    @Operation(summary = "Listar stacks", description = "Lista as tecnologias cadastradas para um projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stacks retornadas."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<List<StackResponse>> listarStacksDoProjeto(@PathVariable("projectId") Long identificadorProjeto) {
        // Lista as stacks ou retorna 404 se o projeto não existir.
        return ResponseEntity.of(servicoDeStacks.listarStacksDoProjeto(identificadorProjeto));
    }

    @PostMapping
    @Operation(summary = "Adicionar stack", description = "Cadastra uma tecnologia e, opcionalmente, sua versão no projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stack criada."),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado.")
    })
    public ResponseEntity<StackResponse> adicionarStackAoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                @RequestBody StackRequest dadosDaStack) {
        // Cria a stack no projeto e informa onde ela pode ser acessada.
        return servicoDeStacks.adicionarStackAoProjeto(identificadorProjeto, dadosDaStack)
                .map(stackAdicionada -> ResponseEntity.created(URI.create(
                        "/api/projects/" + identificadorProjeto + "/stacks/" + stackAdicionada.identificador())).body(stackAdicionada))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{stackId}")
    @Operation(summary = "Atualizar stack", description = "Atualiza o nome e a versão de uma stack do projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stack atualizada."),
            @ApiResponse(responseCode = "404", description = "Projeto ou stack não encontrado.")
    })
    public ResponseEntity<StackResponse> atualizarStackDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                @PathVariable("stackId") Long identificadorStack,
                                                                @RequestBody StackRequest dadosDaStack) {
        // Atualiza a stack se ela for desse projeto.
        return ResponseEntity.of(servicoDeStacks.atualizarStackDoProjeto(
                identificadorProjeto, identificadorStack, dadosDaStack));
    }

    @DeleteMapping("/{stackId}")
    @Operation(summary = "Remover stack", description = "Remove uma stack que pertence ao projeto.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Stack removida."),
            @ApiResponse(responseCode = "404", description = "Stack não encontrada nesse projeto.")
    })
    public ResponseEntity<Void> removerStackDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                     @PathVariable("stackId") Long identificadorStack) {
        // Remove a stack se ela for desse projeto; se não, retorna 404.
        return servicoDeStacks.removerStackDoProjeto(identificadorProjeto, identificadorStack)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
