package br.com.joaojps.jobtrack_api.service;

import br.com.joaojps.jobtrack_api.database.model.ProjectStack;
import br.com.joaojps.jobtrack_api.database.repository.ProjectRepository;
import br.com.joaojps.jobtrack_api.database.repository.ProjectStackRepository;
import br.com.joaojps.jobtrack_api.dto.request.StackRequest;
import br.com.joaojps.jobtrack_api.dto.response.StackResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectStackService {

    private final ProjectRepository repositorioDeProjetos;
    private final ProjectStackRepository repositorioDeStacks;

    public ProjectStackService(ProjectRepository repositorioDeProjetos,
                               ProjectStackRepository repositorioDeStacks) {
        this.repositorioDeProjetos = repositorioDeProjetos;
        this.repositorioDeStacks = repositorioDeStacks;
    }

    @Transactional(readOnly = true)
    public Optional<List<StackResponse>> listarStacksDoProjeto(Long identificadorProjeto) {
        if (!repositorioDeProjetos.existsById(identificadorProjeto)) {
            return Optional.empty();
        }
        return Optional.of(repositorioDeStacks.buscarTodasPorProjetoId(identificadorProjeto).stream()
                .map(this::criarRespostaDaStack).toList());
    }

    public Optional<StackResponse> adicionarStackAoProjeto(Long identificadorProjeto, StackRequest dadosDaStack) {
        if (!repositorioDeProjetos.existsById(identificadorProjeto)) {
            return Optional.empty();
        }
        ProjectStack novaStack = new ProjectStack(
                identificadorProjeto, dadosDaStack.nome(), dadosDaStack.versao());
        return Optional.of(criarRespostaDaStack(repositorioDeStacks.save(novaStack)));
    }

    public Optional<StackResponse> atualizarStackDoProjeto(Long identificadorProjeto,
                                                           Long identificadorStack,
                                                           StackRequest dadosDaStack) {
        return repositorioDeStacks.buscarPorIdentificadorEProjeto(identificadorStack, identificadorProjeto)
                .map(stackEncontrada -> {
            stackEncontrada.atualizarDados(dadosDaStack.nome(), dadosDaStack.versao());
            return criarRespostaDaStack(repositorioDeStacks.save(stackEncontrada));
        });
    }

    public boolean removerStackDoProjeto(Long identificadorProjeto, Long identificadorStack) {
        Optional<ProjectStack> stackEncontrada = repositorioDeStacks.buscarPorIdentificadorEProjeto(
                identificadorStack, identificadorProjeto);
        if (stackEncontrada.isEmpty()) {
            return false;
        }
        repositorioDeStacks.delete(stackEncontrada.get());
        return true;
    }

    private StackResponse criarRespostaDaStack(ProjectStack stack) {
        return new StackResponse(stack.getIdentificador(), stack.getIdentificadorProjeto(),
                stack.getNome(), stack.getVersao());
    }
}
