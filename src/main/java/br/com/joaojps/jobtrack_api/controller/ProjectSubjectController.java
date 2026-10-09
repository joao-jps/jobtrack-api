package br.com.joaojps.jobtrack_api.controller;

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
public class ProjectSubjectController {

    private final ProjectSubjectService servicoDeMaterias;

    public ProjectSubjectController(ProjectSubjectService servicoDeMaterias) {
        this.servicoDeMaterias = servicoDeMaterias;
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> listarMateriasDoProjeto(@PathVariable("projectId") Long identificadorProjeto) {
        return ResponseEntity.of(servicoDeMaterias.listarMateriasDoProjeto(identificadorProjeto));
    }

    @PostMapping
    public ResponseEntity<SubjectResponse> adicionarMateriaAoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                    @RequestBody SubjectRequest dadosDaMateria) {
        return servicoDeMaterias.adicionarMateriaAoProjeto(identificadorProjeto, dadosDaMateria)
                .map(materiaAdicionada -> ResponseEntity.created(URI.create(
                        "/api/projects/" + identificadorProjeto + "/subjects/" + materiaAdicionada.identificador())).body(materiaAdicionada))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectResponse> atualizarMateriaDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                                    @PathVariable("subjectId") Long identificadorMateria,
                                                                    @RequestBody SubjectRequest dadosDaMateria) {
        return ResponseEntity.of(servicoDeMaterias.atualizarMateriaDoProjeto(
                identificadorProjeto, identificadorMateria, dadosDaMateria));
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> removerMateriaDoProjeto(@PathVariable("projectId") Long identificadorProjeto,
                                                       @PathVariable("subjectId") Long identificadorMateria) {
        return servicoDeMaterias.removerMateriaDoProjeto(identificadorProjeto, identificadorMateria)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
