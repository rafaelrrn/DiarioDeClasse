package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Tabela de associação entre Pessoa e Contato (N:N com atributo 'nome'). */
@Entity
@Table(name = "contato_pessoa")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContatoPessoa extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contato_pessoa")
    private Long idContatoPessoa;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_pessoa"))
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "id_contato", nullable = false, referencedColumnName = "id_contato",
            foreignKey = @ForeignKey(name = "fk_id_contato"))
    private Contato contato;

    @Column(name = "nome")
    private String nome;
}
