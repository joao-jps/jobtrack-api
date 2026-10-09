package br.com.joaojps.jobtrack_api.dto.request;

import br.com.joaojps.jobtrack_api.database.model.ProjectStatus;

// Recebe o novo status para mudar só essa parte do projeto.
public record StatusRequest(ProjectStatus status) { }
