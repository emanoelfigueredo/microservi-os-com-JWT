package com.efigueredo.serviceanotacoes.infra.handler.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnotacaoException extends RuntimeException {

    private String title;
    private String detail;
    private String type;
    private String status;

}
