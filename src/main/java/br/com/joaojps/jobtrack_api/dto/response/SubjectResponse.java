package br.com.joaojps.jobtrack_api.dto.response;

// Dados da matéria que a API devolve, com o ID do projeto dela.
public record SubjectResponse(Long identificador, Long identificadorProjeto, String nome, String descricao) { }
