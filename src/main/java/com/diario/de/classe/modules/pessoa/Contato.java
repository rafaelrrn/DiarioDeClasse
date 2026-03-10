package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Representa um meio de contato (telefone, e-mail, etc.) sem vínculo direto com uma Pessoa. */
@Entity
@Table(name = "contato")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Contato extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contato")
    private Long idContato;

    @Column(name = "tipo_contato")
    private String tipoContato;

    @Column(name = "contato")
    private String contato;
}
