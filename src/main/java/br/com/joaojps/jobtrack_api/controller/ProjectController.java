package br.com.joaojps.jobtrack_api.controller;

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
public class ProjectController {

    private final ProjectService servicoDeProjetos;

    public ProjectController(ProjectService servicoDeProjetos) {
        this.servicoDeProjetos = servicoDeProjetos;
    }

    @GetMapping
    public List<ProjectResponse> listarProjetos() {
        return servicoDeProjetos.listarProjetos();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> buscarProjetoPorIdentificador(@PathVariable("projectId") Long identificadorProjeto) {
        return ResponseEntity.of(servicoDeProjetos.buscarProjetoPorIdentificador(identificadorProjeto));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> adicionarProjeto(@RequestBody ProjectRequest dadosDoProjeto) {
        ProjectResponse projetoAdicionado = servicoDeProjetos.adicionarProjeto(dadosDoProjeto);
        return ResponseEntity.created(URI.create("/api/projects/" + projetoAdicionado.identificador())).body(projetoAdicionado);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> atualizarProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                           @RequestBody ProjectRequest dadosDoProjeto) {
        return ResponseEntity.of(servicoDeProjetos.atualizarProjeto(identificadorProjeto, dadosDoProjeto));
    }

    @PatchMapping("/{projectId}/status")
    public ResponseEntity<ProjectResponse> atualizarStatusProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                 @RequestBody StatusRequest dadosDoStatus) {
        ProjectStatus novoStatus = dadosDoStatus.status();
        return ResponseEntity.of(servicoDeProjetos.atualizarStatusProjeto(identificadorProjeto, novoStatus));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> removerProjeto(@PathVariable("projectId") Long identificadorProjeto) {
        return servicoDeProjetos.removerProjeto(identificadorProjeto)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
