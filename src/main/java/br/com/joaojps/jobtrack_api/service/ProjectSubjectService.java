package br.com.joaojps.jobtrack_api.service;

import br.com.joaojps.jobtrack_api.database.model.ProjectSubject;
import br.com.joaojps.jobtrack_api.database.repository.ProjectRepository;
import br.com.joaojps.jobtrack_api.database.repository.ProjectSubjectRepository;
import br.com.joaojps.jobtrack_api.dto.request.SubjectRequest;
import br.com.joaojps.jobtrack_api.dto.response.SubjectResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectSubjectService {

    private final ProjectRepository repositorioDeProjetos;
    private final ProjectSubjectRepository repositorioDeMaterias;

    public ProjectSubjectService(ProjectRepository repositorioDeProjetos,
                                 ProjectSubjectRepository repositorioDeMaterias) {
        this.repositorioDeProjetos = repositorioDeProjetos;
        this.repositorioDeMaterias = repositorioDeMaterias;
    }

    @Transactional(readOnly = true)
    public Optional<List<SubjectResponse>> listarMateriasDoProjeto(Long identificadorProjeto) {
        if (!repositorioDeProjetos.existsById(identificadorProjeto)) {
            return Optional.empty();
        }
        return Optional.of(repositorioDeMaterias.buscarTodasPorProjetoId(identificadorProjeto).stream()
                .map(this::criarRespostaDaMateria).toList());
    }

    public Optional<SubjectResponse> adicionarMateriaAoProjeto(Long identificadorProjeto, SubjectRequest dadosDaMateria) {
        if (!repositorioDeProjetos.existsById(identificadorProjeto)) {
            return Optional.empty();
        }
        ProjectSubject novaMateria = new ProjectSubject(
                identificadorProjeto, dadosDaMateria.nome(), dadosDaMateria.descricao());
        return Optional.of(criarRespostaDaMateria(repositorioDeMaterias.save(novaMateria)));
    }

    public Optional<SubjectResponse> atualizarMateriaDoProjeto(Long identificadorProjeto,
                                                               Long identificadorMateria,
                                                               SubjectRequest dadosDaMateria) {
        return repositorioDeMaterias.buscarPorIdentificadorEProjeto(identificadorMateria, identificadorProjeto)
                .map(materiaEncontrada -> {
            materiaEncontrada.atualizarDados(dadosDaMateria.nome(), dadosDaMateria.descricao());
            return criarRespostaDaMateria(repositorioDeMaterias.save(materiaEncontrada));
        });
    }

    public boolean removerMateriaDoProjeto(Long identificadorProjeto, Long identificadorMateria) {
        Optional<ProjectSubject> materiaEncontrada = repositorioDeMaterias.buscarPorIdentificadorEProjeto(
                identificadorMateria, identificadorProjeto);
        if (materiaEncontrada.isEmpty()) {
            return false;
        }
        repositorioDeMaterias.delete(materiaEncontrada.get());
        return true;
    }

    private SubjectResponse criarRespostaDaMateria(ProjectSubject materia) {
        return new SubjectResponse(materia.getIdentificador(), materia.getIdentificadorProjeto(),
                materia.getNome(), materia.getDescricao());
    }
}
