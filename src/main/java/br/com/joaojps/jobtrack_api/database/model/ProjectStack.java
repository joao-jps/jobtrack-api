package br.com.joaojps.jobtrack_api.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "projeto_stack")
// Guarda uma stack e o ID do projeto ao qual ela pertence.
public class ProjectStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String version;

    protected ProjectStack() {
        // O JPA usa este construtor para carregar a stack do banco.
    }

    public ProjectStack(Long identificadorProjeto, String nome, String versao) {
        // Cria a stack já ligada ao projeto da rota.
        this.projectId = identificadorProjeto;
        this.name = nome;
        this.version = versao;
    }

    // Estes getters entregam os dados usados nas respostas da API.
    public Long getIdentificador() { return id; } // Retorna o ID da stack.
    public Long getIdentificadorProjeto() { return projectId; } // Retorna o ID do projeto.
    public String getNome() { return name; } // Retorna o nome da tecnologia.
    public String getVersao() { return version; } // Retorna a versão, se foi informada.

    public void atualizarDados(String nome, String versao) {
        // Muda o nome e a versão sem trocar o projeto.
        this.name = nome;
        this.version = versao;
    }
}
