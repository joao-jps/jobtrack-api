package br.com.joaojps.jobtrack_api.dto.request;

// Dados que a API recebe para criar ou atualizar uma matéria.
public record SubjectRequest(String nome, String descricao) { }
