package br.com.joaojps.jobtrack_api.dto.response;

// Dados da stack que a API devolve, com o ID do projeto dela.
public record StackResponse(Long identificador, Long identificadorProjeto, String nome, String versao) { }
