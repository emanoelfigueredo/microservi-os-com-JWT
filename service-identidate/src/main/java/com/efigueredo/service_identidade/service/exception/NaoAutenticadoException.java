package com.efigueredo.service_identidade.service.exception;

public class NaoAutenticadoException extends IdentidadeException {
    public NaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}
