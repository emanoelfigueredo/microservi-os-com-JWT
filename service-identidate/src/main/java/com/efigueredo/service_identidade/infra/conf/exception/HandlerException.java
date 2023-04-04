package com.efigueredo.service_identidade.infra.conf.exception;

import com.efigueredo.service_identidade.service.exception.NaoAutenticadoException;
import com.efigueredo.service_identidade.service.exception.UsernameExistenteException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
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

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<DtoErro> tratarSignatureException(SignatureException ex) {
        DtoErro erro = new DtoErro("Falha de autenticacao", "Token JWT invalido", "", "401");
        return ResponseEntity.status(401).body(erro);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<DtoErro> tratarExpiredJwtException(ExpiredJwtException ex) {
        DtoErro erro = new DtoErro("Falha de autenticacao", "Token JWT expirado", "", "401");
        return ResponseEntity.status(401).body(erro);
    }

}
