package br.com.joaojps.jobtrack_api.service;

import br.com.joaojps.jobtrack_api.database.model.Project;
import br.com.joaojps.jobtrack_api.database.model.ProjectStatus;
import br.com.joaojps.jobtrack_api.database.repository.ProjectRepository;
import br.com.joaojps.jobtrack_api.database.repository.ProjectStackRepository;
import br.com.joaojps.jobtrack_api.database.repository.ProjectSubjectRepository;
import br.com.joaojps.jobtrack_api.dto.request.ProjectRequest;
import br.com.joaojps.jobtrack_api.dto.response.ProjectResponse;
import br.com.joaojps.jobtrack_api.dto.response.StackResponse;
import br.com.joaojps.jobtrack_api.dto.response.SubjectResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository repositorioDeProjetos;
    private final ProjectSubjectRepository repositorioDeMaterias;
    private final ProjectStackRepository repositorioDeStacks;

    public ProjectService(ProjectRepository repositorioDeProjetos,
                          ProjectSubjectRepository repositorioDeMaterias,
                          ProjectStackRepository repositorioDeStacks) {
        this.repositorioDeProjetos = repositorioDeProjetos;
        this.repositorioDeMaterias = repositorioDeMaterias;
        this.repositorioDeStacks = repositorioDeStacks;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listarProjetos() {
        return repositorioDeProjetos.findAll().stream().map(this::criarRespostaDoProjeto).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ProjectResponse> buscarProjetoPorIdentificador(Long identificadorProjeto) {
        return repositorioDeProjetos.findById(identificadorProjeto).map(this::criarRespostaDoProjeto);
    }

    public ProjectResponse adicionarProjeto(ProjectRequest dadosDoProjeto) {
        Project novoProjeto = new Project(dadosDoProjeto.nome(), dadosDoProjeto.objetivo(), dadosDoProjeto.status());
        return criarRespostaDoProjeto(repositorioDeProjetos.save(novoProjeto));
    }

    public Optional<ProjectResponse> atualizarProjeto(Long identificadorProjeto, ProjectRequest dadosDoProjeto) {
        return repositorioDeProjetos.findById(identificadorProjeto).map(projetoEncontrado -> {
            projetoEncontrado.atualizarDados(dadosDoProjeto.nome(), dadosDoProjeto.objetivo(), dadosDoProjeto.status());
            return criarRespostaDoProjeto(repositorioDeProjetos.save(projetoEncontrado));
        });
    }

    public Optional<ProjectResponse> atualizarStatusProjeto(Long identificadorProjeto, ProjectStatus novoStatus) {
        return repositorioDeProjetos.findById(identificadorProjeto).map(projetoEncontrado -> {
            projetoEncontrado.atualizarStatus(novoStatus);
            return criarRespostaDoProjeto(repositorioDeProjetos.save(projetoEncontrado));
        });
    }

    public boolean removerProjeto(Long identificadorProjeto) {
        if (!repositorioDeProjetos.existsById(identificadorProjeto)) {
            return false;
        }
        repositorioDeProjetos.deleteById(identificadorProjeto);
        return true;
    }

    private ProjectResponse criarRespostaDoProjeto(Project projeto) {
        List<SubjectResponse> materiasDoProjeto = repositorioDeMaterias.buscarTodasPorProjetoId(projeto.getIdentificador())
                .stream()
                .map(materia -> new SubjectResponse(materia.getIdentificador(), materia.getIdentificadorProjeto(),
                        materia.getNome(), materia.getDescricao()))
                .toList();
        List<StackResponse> stacksDoProjeto = repositorioDeStacks.buscarTodasPorProjetoId(projeto.getIdentificador())
                .stream()
                .map(stack -> new StackResponse(stack.getIdentificador(), stack.getIdentificadorProjeto(),
                        stack.getNome(), stack.getVersao()))
                .toList();

        return new ProjectResponse(projeto.getIdentificador(), projeto.getNome(), projeto.getObjetivo(),
                projeto.getStatus(), projeto.getDataCriacao(), projeto.getDataAtualizacao(),
                materiasDoProjeto, stacksDoProjeto);
    }
}
