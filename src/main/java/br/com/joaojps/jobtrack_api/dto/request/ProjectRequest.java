package br.com.joaojps.jobtrack_api.dto.request;

import br.com.joaojps.jobtrack_api.database.model.ProjectStatus;

public record ProjectRequest(String nome, String objetivo, ProjectStatus status) { }
