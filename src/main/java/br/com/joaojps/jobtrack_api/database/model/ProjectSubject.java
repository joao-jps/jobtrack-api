package br.com.joaojps.jobtrack_api.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "projeto_materia")
// Guarda uma matéria e o ID do projeto ao qual ela pertence.
public class ProjectSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    protected ProjectSubject() {
        // O JPA usa este construtor para carregar a matéria do banco.
    }

    public ProjectSubject(Long identificadorProjeto, String nome, String descricao) {
        // Cria a matéria já ligada ao projeto da rota.
        this.projectId = identificadorProjeto;
        this.name = nome;
        this.description = descricao;
    }

    // Estes getters entregam os dados usados nas respostas da API.
    public Long getIdentificador() { return id; } // Retorna o ID da matéria.
    public Long getIdentificadorProjeto() { return projectId; } // Retorna o ID do projeto.
    public String getNome() { return name; } // Retorna o nome da matéria.
    public String getDescricao() { return description; } // Retorna a descrição, se foi informada.

    public void atualizarDados(String nome, String descricao) {
        // Muda os dados da matéria sem trocar o projeto.
        this.name = nome;
        this.description = descricao;
    }
}
