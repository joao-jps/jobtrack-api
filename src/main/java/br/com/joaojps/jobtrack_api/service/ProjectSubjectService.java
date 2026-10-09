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
// Confere o projeto, cuida das matérias e prepara as respostas da API.
public class ProjectSubjectService {

    private final ProjectRepository repositorioDeProjetos;
    private final ProjectSubjectRepository repositorioDeMaterias;

    public ProjectSubjectService(ProjectRepository repositorioDeProjetos,
                                 ProjectSubjectRepository repositorioDeMaterias) {
        // Guarda os repositórios usados para buscar projetos e salvar matérias.
        this.repositorioDeProjetos = repositorioDeProjetos;
        this.repositorioDeMaterias = repositorioDeMaterias;
    }

    @Transactional(readOnly = true)
    public Optional<List<SubjectResponse>> listarMateriasDoProjeto(Long identificadorProjeto) {
        // Confere se o projeto existe antes de listar as matérias.
        if (!repositorioDeProjetos.existsById(identificadorProjeto)) {
            return Optional.empty();
        }
        return Optional.of(repositorioDeMaterias.buscarTodasPorProjetoId(identificadorProjeto).stream()
                .map(this::criarRespostaDaMateria).toList());
    }

    public Optional<SubjectResponse> adicionarMateriaAoProjeto(Long identificadorProjeto, SubjectRequest dadosDaMateria) {
        // Só salva a matéria se o projeto existir.
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
        // Confere se a matéria é desse projeto antes de atualizar.
        return repositorioDeMaterias.buscarPorIdentificadorEProjeto(identificadorMateria, identificadorProjeto)
                .map(materiaEncontrada -> {
            materiaEncontrada.atualizarDados(dadosDaMateria.nome(), dadosDaMateria.descricao());
            return criarRespostaDaMateria(repositorioDeMaterias.save(materiaEncontrada));
        });
    }

    public boolean removerMateriaDoProjeto(Long identificadorProjeto, Long identificadorMateria) {
        // Só remove a matéria se ela for desse projeto.
        Optional<ProjectSubject> materiaEncontrada = repositorioDeMaterias.buscarPorIdentificadorEProjeto(
                identificadorMateria, identificadorProjeto);
        if (materiaEncontrada.isEmpty()) {
            return false;
        }
        repositorioDeMaterias.delete(materiaEncontrada.get());
        return true;
    }

    private SubjectResponse criarRespostaDaMateria(ProjectSubject materia) {
        // Prepara os dados da matéria para a resposta da API.
        return new SubjectResponse(materia.getIdentificador(), materia.getIdentificadorProjeto(),
                materia.getNome(), materia.getDescricao());
    }
}
