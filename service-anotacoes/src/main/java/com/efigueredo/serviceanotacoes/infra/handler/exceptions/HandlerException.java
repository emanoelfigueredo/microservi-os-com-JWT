package com.efigueredo.serviceanotacoes.infra.handler.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DtoErro>> tratarErroDeSintaxeNoJson(MethodArgumentNotValidException ex) {
        List<FieldError> erros = ex.getFieldErrors();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                erros
                        .stream()
                        .map(DtoErro::new)
                        .toList()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DtoErro> tratarErroDeSintaxeNoJson(EntityNotFoundException ex) {
        String mensagem = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new DtoErro("Anotacao inexistente", mensagem, "", "404")
        );
    }

    @ExceptionHandler(AnotacaoException.class)
    public ResponseEntity<DtoErro> tratarErroAnotacaoException(AnotacaoException ex) {
        int status = Integer.parseInt(ex.getStatus());
        return ResponseEntity.status(status).body(
                new DtoErro(ex.getTitle(), ex.getDetail(), ex.getType(), ex.getStatus())
        );
    }

}
