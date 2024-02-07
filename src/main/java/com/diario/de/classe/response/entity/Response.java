package com.diario.de.classe.response.entity;

import lombok.Data;

@Data
public class Response<T> {
    private Meta meta;
    private T results;
}