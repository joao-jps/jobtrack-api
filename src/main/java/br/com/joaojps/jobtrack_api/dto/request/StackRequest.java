package br.com.joaojps.jobtrack_api.dto.request;

// Dados que a API recebe para criar ou atualizar uma stack.
public record StackRequest(String nome, String versao) { }
