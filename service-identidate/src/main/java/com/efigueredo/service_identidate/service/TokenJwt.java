package com.efigueredo.service_identidate.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenJwt {

    private String token;
    private Date momentoCriacao;
    private Date momentoExpiracao;

}
