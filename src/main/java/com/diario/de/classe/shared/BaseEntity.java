package com.diario.de.classe.shared;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Classe base para todas as entidades JPA do sistema.
 *
 * Centraliza os campos de auditoria (createdAt, updatedAt) e soft delete
 * (deletedAt, ativo). Toda entidade deve estender esta classe.
 *
 * A auditoria automática de createdAt e updatedAt é gerenciada pelo
 * Spring Data JPA (@EnableJpaAuditing na classe Application).
 *
 * Soft delete: nunca usar repository.delete() — chamar desativar() no Service,
 * que seta ativo=false e deletedAt com a data atual.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    /**
     * Data/hora de criação do registro — preenchida automaticamente pelo JPA Auditing.
     * Nunca atualizada após a inserção (updatable = false).
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Data/hora da última modificação — atualizada automaticamente pelo JPA Auditing
     * a cada save() no repositório.
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Data/hora em que o registro foi desativado (soft delete).
     * null enquanto o registro estiver ativo.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Indica se o registro está ativo no sistema.
     * false = registro "deletado" via soft delete — nunca exibir ao usuário.
     */
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}
