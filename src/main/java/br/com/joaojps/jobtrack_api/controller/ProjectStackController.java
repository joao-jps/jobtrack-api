package br.com.joaojps.jobtrack_api.controller;

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
public class ProjectStackController {

    private final ProjectStackService servicoDeStacks;

    public ProjectStackController(ProjectStackService servicoDeStacks) {
        this.servicoDeStacks = servicoDeStacks;
    }

    @GetMapping
    public ResponseEntity<List<StackResponse>> listarStacksDoProjeto(@PathVariable("projectId") Long identificadorProjeto) {
        return ResponseEntity.of(servicoDeStacks.listarStacksDoProjeto(identificadorProjeto));
    }

    @PostMapping
    public ResponseEntity<StackResponse> adicionarStackAoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                @RequestBody StackRequest dadosDaStack) {
        return servicoDeStacks.adicionarStackAoProjeto(identificadorProjeto, dadosDaStack)
                .map(stackAdicionada -> ResponseEntity.created(URI.create(
                        "/api/projects/" + identificadorProjeto + "/stacks/" + stackAdicionada.identificador())).body(stackAdicionada))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{stackId}")
    public ResponseEntity<StackResponse> atualizarStackDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                @PathVariable("stackId") Long identificadorStack,
                                                                @RequestBody StackRequest dadosDaStack) {
        return ResponseEntity.of(servicoDeStacks.atualizarStackDoProjeto(
                identificadorProjeto, identificadorStack, dadosDaStack));
    }

    @DeleteMapping("/{stackId}")
    public ResponseEntity<Void> removerStackDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                     @PathVariable("stackId") Long identificadorStack) {
        return servicoDeStacks.removerStackDoProjeto(identificadorProjeto, identificadorStack)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
