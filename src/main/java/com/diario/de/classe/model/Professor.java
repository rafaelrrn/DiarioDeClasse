package com.diario.de.classe.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "PRF000")
@Data
@EntityListeners(AuditingEntityListener.class)

public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CodPrf")
    private Long codPrf;

    @Column(name="PrfNme")
    private String prfNme;
}
