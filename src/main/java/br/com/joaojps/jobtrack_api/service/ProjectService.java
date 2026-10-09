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
// Cuida das operações de projeto e monta as respostas com seus dados.
public class ProjectService {

    private final ProjectRepository repositorioDeProjetos;
    private final ProjectSubjectRepository repositorioDeMaterias;
    private final ProjectStackRepository repositorioDeStacks;

    public ProjectService(ProjectRepository repositorioDeProjetos,
                          ProjectSubjectRepository repositorioDeMaterias,
                          ProjectStackRepository repositorioDeStacks) {
        // Guarda os repositórios usados para buscar projetos, matérias e stacks.
        this.repositorioDeProjetos = repositorioDeProjetos;
        this.repositorioDeMaterias = repositorioDeMaterias;
        this.repositorioDeStacks = repositorioDeStacks;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> listarProjetos() {
        // Busca todos os projetos e prepara cada um para a resposta da API.
        return repositorioDeProjetos.findAll().stream().map(this::criarRespostaDoProjeto).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ProjectResponse> buscarProjetoPorIdentificador(Long identificadorProjeto) {
        // Busca pelo ID; se não achar, devolve um resultado vazio.
        return repositorioDeProjetos.findById(identificadorProjeto).map(this::criarRespostaDoProjeto);
    }

    public ProjectResponse adicionarProjeto(ProjectRequest dadosDoProjeto) {
        // Cria o projeto, salva no banco e prepara a resposta.
        Project novoProjeto = new Project(dadosDoProjeto.nome(), dadosDoProjeto.objetivo(), dadosDoProjeto.status());
        return criarRespostaDoProjeto(repositorioDeProjetos.save(novoProjeto));
    }

    public Optional<ProjectResponse> atualizarProjeto(Long identificadorProjeto, ProjectRequest dadosDoProjeto) {
        // Busca o projeto, atualiza os dados e devolve vazio se não achar.
        return repositorioDeProjetos.findById(identificadorProjeto).map(projetoEncontrado -> {
            projetoEncontrado.atualizarDados(dadosDoProjeto.nome(), dadosDoProjeto.objetivo(), dadosDoProjeto.status());
            return criarRespostaDoProjeto(repositorioDeProjetos.save(projetoEncontrado));
        });
    }

    public Optional<ProjectResponse> atualizarStatusProjeto(Long identificadorProjeto, ProjectStatus novoStatus) {
        // Busca o projeto e muda só o status.
        return repositorioDeProjetos.findById(identificadorProjeto).map(projetoEncontrado -> {
            projetoEncontrado.atualizarStatus(novoStatus);
            return criarRespostaDoProjeto(repositorioDeProjetos.save(projetoEncontrado));
        });
    }

    public boolean removerProjeto(Long identificadorProjeto) {
        // Confere se o projeto existe antes de excluir e informa se conseguiu.
        if (!repositorioDeProjetos.existsById(identificadorProjeto)) {
            return false;
        }
        repositorioDeProjetos.deleteById(identificadorProjeto);
        return true;
    }

    private ProjectResponse criarRespostaDoProjeto(Project projeto) {
        // Monta a resposta sem devolver direto os dados internos do banco.
        // Junta as matérias e stacks para incluir tudo na resposta do projeto.
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
