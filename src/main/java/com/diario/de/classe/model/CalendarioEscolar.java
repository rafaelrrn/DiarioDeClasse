package com.diario.de.classe.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


//Anotação do springboot para informar que é classe é uma entidade/tabela
@Entity

//Informando ao springboot qual o nome da tabela no banco
@Table(name = "calendario_escolar")

//Anotação do springboot/lombok para realizar os getters e setters
@Data

//----
@ToString

@EntityListeners(AuditingEntityListener.class)
public class CalendarioEscolar {

    //Anotação do springboot que informa que o campo abaixo é a chave primária
    @Id

    //Anotação do springboot que determina a forma/estratégia do auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //Anotação utilizada para informar o nome das colunas da tabela
    @Column(name="id_calendario_escolar")

    //Apelido da coluna do banco para o java
    private Long idCalendarioEscolar;


    @ManyToOne
    @JoinColumn(name = "id_mes", nullable = false, referencedColumnName = "id_mes", foreignKey = @ForeignKey(name = "fk_id_mes"))
    private Mes mes;

    @ManyToOne
    @JoinColumn(name = "id_ano_calendario", referencedColumnName = "id_ano_calendario", foreignKey = @ForeignKey(name = "fk_id_ano_calendario"))
    private AnoCalendario anoCalendario;

    @ManyToOne
    @JoinColumn(name = "id_periodo", nullable = false, referencedColumnName = "id_periodo", foreignKey = @ForeignKey(name = "fk_id_periodo"))
    private Periodo periodo;

    @ManyToOne
    @JoinColumn(name = "id_classe", nullable = false, referencedColumnName = "id_classe", foreignKey = @ForeignKey(name = "fk_id_classe"))
    private Classe classe;

    @Column(name="dias_letivos")
    private String diasLetivos;

    @Column(name="dias_avaliacoes")
    private String diasAvaliacoes;

    @Column(name = "created_at" ,nullable = false, updatable = false)

    //Padronização para o tipo de data da coluna acima
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

}
