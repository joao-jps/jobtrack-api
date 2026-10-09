package br.com.joaojps.jobtrack_api.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "projeto")
// Guarda os dados do projeto; atualiza as datas quando ele é criado ou editado.
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String objective;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Project() {
        // O JPA usa este construtor para carregar o projeto do banco.
    }

    public Project(String nome, String objetivo, ProjectStatus status) {
        // Cria o projeto; as datas são preenchidas quando ele for salvo.
        this.name = nome;
        this.objective = objetivo;
        this.status = status;
    }

    @PrePersist
    void preencherDatasDeCriacao() {
        // Preenche as datas quando o projeto é salvo pela primeira vez.
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void atualizarDataDeAtualizacao() {
        // Atualiza a data quando o projeto é editado.
        updatedAt = LocalDateTime.now();
    }

    // Estes getters usam nomes em português no resto da aplicação.
    public Long getIdentificador() { return id; } // Retorna o ID do projeto.
    public String getNome() { return name; } // Retorna o nome do projeto.
    public String getObjetivo() { return objective; } // Retorna o objetivo do projeto.
    public ProjectStatus getStatus() { return status; } // Retorna o status atual.
    public LocalDateTime getDataCriacao() { return createdAt; } // Retorna quando foi criado.
    public LocalDateTime getDataAtualizacao() { return updatedAt; } // Retorna quando foi editado.

    public void atualizarDados(String nome, String objetivo, ProjectStatus status) {
        // Atualiza os dados sem mexer no ID nem nas datas.
        this.name = nome;
        this.objective = objetivo;
        this.status = status;
    }

    public void atualizarStatus(ProjectStatus status) {
        // Muda o status e mantém os outros dados como estão.
        this.status = status;
    }
}
