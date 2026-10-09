package br.com.joaojps.jobtrack_api.dto.request;

import br.com.joaojps.jobtrack_api.database.model.ProjectStatus;

// Dados que a API recebe para criar ou atualizar um projeto.
public record ProjectRequest(String nome, String objetivo, ProjectStatus status) { }
