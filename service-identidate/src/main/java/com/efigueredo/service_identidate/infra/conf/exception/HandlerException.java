package com.efigueredo.service_identidate.infra.conf.exception;

import com.efigueredo.service_identidate.service.exception.NaoAutenticadoException;
import com.efigueredo.service_identidate.service.exception.UsernameExistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DtoErro>> tratarMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<DtoErro> listaDtoErros = ex.getFieldErrors().stream().map(DtoErro::new).toList();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(listaDtoErros);
    }

    @ExceptionHandler(UsernameExistenteException.class)
    public ResponseEntity<DtoErro> tratarUsernameExistenteException(UsernameExistenteException ex) {
        DtoErro erro = new DtoErro("Falha em registrar usuário", ex.getMessage(), "", "400");
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(NaoAutenticadoException.class)
    public ResponseEntity<DtoErro> tratarUsernameExistenteException(NaoAutenticadoException ex) {
        DtoErro erro = new DtoErro("Autenticacao falhou", ex.getMessage(), "", "401");
        return ResponseEntity.status(401).body(erro);
    }

}
