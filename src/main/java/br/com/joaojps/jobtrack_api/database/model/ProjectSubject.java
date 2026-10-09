package br.com.joaojps.jobtrack_api.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "projeto_materia")
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
    }

    public ProjectSubject(Long identificadorProjeto, String nome, String descricao) {
        this.projectId = identificadorProjeto;
        this.name = nome;
        this.description = descricao;
    }

    public Long getIdentificador() { return id; }
    public Long getIdentificadorProjeto() { return projectId; }
    public String getNome() { return name; }
    public String getDescricao() { return description; }

    public void atualizarDados(String nome, String descricao) {
        this.name = nome;
        this.description = descricao;
    }
}
