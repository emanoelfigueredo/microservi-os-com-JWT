package com.efigueredo.service_identidade.service.exception;

public class UsernameExistenteException extends IdentidadeException {
    public UsernameExistenteException(String mensagem) {
        super(mensagem);
    }
}
