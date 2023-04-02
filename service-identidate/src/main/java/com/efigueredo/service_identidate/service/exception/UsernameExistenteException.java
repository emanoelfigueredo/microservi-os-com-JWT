package com.efigueredo.service_identidate.service.exception;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsernameExistenteException extends IdentidadeException {
    public UsernameExistenteException(String mensagem) {
        super(mensagem);
    }
}
