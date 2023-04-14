package com.efigueredo.service_identidade.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenJwt {

    private String token;
    private Date momentoCriacao;
    private Date momentoExpiracao;

}
