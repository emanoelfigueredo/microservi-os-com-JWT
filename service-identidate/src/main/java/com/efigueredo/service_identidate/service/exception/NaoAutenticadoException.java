package com.efigueredo.service_identidate.service.exception;

public class NaoAutenticadoException extends IdentidadeException {
    public NaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}
