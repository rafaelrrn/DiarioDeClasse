package com.diario.de.classe.response.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Meta {
    public static final String LIST = "list";
    public static final String OBJECT = "object";

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String stage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer page;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer rpp;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer size;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String type;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String version;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long duration;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String message;
}