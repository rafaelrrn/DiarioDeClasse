package com.diario.de.classe.dto;

import com.diario.de.classe.model.Endereco;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class EnderecoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idEndereco;

    @Size(max = 2)
    private String uf;

    @Size(max = 255)
    private String cidade;

    @Size(max = 255)
    private String bairro;

    @Size(max = 255)
    private String rua;

    @Size(max = 10)
    private String numero;

    @Size(max = 9)
    private String cep;

    @Size(max = 255)
    private String complemento;

    private Date createdAt;
    private Date updatedAt;

    public EnderecoDTO() {}

    public EnderecoDTO(Endereco entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
} 