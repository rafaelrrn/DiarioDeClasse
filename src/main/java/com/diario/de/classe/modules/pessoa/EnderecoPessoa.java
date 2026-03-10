package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Tabela de associação entre Pessoa e Endereco (N:N com atributo 'nome'). */
@Entity
@Table(name = "endereco_pessoa")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class EnderecoPessoa extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco_pessoa")
    private Long idEnderecoPessoa;

    @ManyToOne
    @JoinColumn(name = "id_pessoa", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_pessoa"))
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "id_endereco", nullable = false, referencedColumnName = "id_endereco",
            foreignKey = @ForeignKey(name = "fk_id_endereco"))
    private Endereco endereco;

    @Column(name = "nome")
    private String nome;
}
