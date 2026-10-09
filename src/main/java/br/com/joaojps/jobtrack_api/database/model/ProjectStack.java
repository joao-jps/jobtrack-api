package br.com.joaojps.jobtrack_api.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "projeto_stack")
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
    }

    public ProjectStack(Long identificadorProjeto, String nome, String versao) {
        this.projectId = identificadorProjeto;
        this.name = nome;
        this.version = versao;
    }

    public Long getIdentificador() { return id; }
    public Long getIdentificadorProjeto() { return projectId; }
    public String getNome() { return name; }
    public String getVersao() { return version; }

    public void atualizarDados(String nome, String versao) {
        this.name = nome;
        this.version = versao;
    }
}
