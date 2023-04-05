package com.efigueredo.service_identidade.infra.conf.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdentityException extends RuntimeException {

    private String title;
    private String detail;
    private String type;
    private String status;

}
