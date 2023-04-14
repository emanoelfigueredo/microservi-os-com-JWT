package com.efigueredo.service_identidade.infra.conf.exception;

import org.springframework.validation.FieldError;

public record DtoErro(String title, String detail, String type, String status) {

    public DtoErro(FieldError erro) {
      this("Campo invalido", erro.getDefaultMessage(), "", "422");
    }

}
