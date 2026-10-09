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
    }

    public Project(String nome, String objetivo, ProjectStatus status) {
        this.name = nome;
        this.objective = objetivo;
        this.status = status;
    }

    @PrePersist
    void preencherDatasDeCriacao() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void atualizarDataDeAtualizacao() {
        updatedAt = LocalDateTime.now();
    }

    public Long getIdentificador() { return id; }
    public String getNome() { return name; }
    public String getObjetivo() { return objective; }
    public ProjectStatus getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return createdAt; }
    public LocalDateTime getDataAtualizacao() { return updatedAt; }

    public void atualizarDados(String nome, String objetivo, ProjectStatus status) {
        this.name = nome;
        this.objective = objetivo;
        this.status = status;
    }

    public void atualizarStatus(ProjectStatus status) {
        this.status = status;
    }
}
