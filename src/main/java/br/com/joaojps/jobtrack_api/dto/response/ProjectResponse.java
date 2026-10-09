package br.com.joaojps.jobtrack_api.dto.response;

import br.com.joaojps.jobtrack_api.database.model.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;

// Resposta com os dados do projeto, suas matérias e suas stacks.
public record ProjectResponse(
        Long identificador,
        String nome,
        String objetivo,
        ProjectStatus status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        List<SubjectResponse> materias,
        List<StackResponse> stacks
) { }
