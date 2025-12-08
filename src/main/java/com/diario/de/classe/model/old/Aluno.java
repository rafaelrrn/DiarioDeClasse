package com.diario.de.classe.model.old;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ALN004")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString

public class Aluno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CodAln")
    private Long codAln;

    @Column(name="AlnNme")
    private String alnNme;

    @Column(name="NroCmd")
    private Long nroCmd;

    @Column(name="CodCls")
    private Long codCls;
}
