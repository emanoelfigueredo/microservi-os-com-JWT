package com.efigueredo.service_gateway.infra.filter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatewayException extends RuntimeException {

    private String title;
    private String detail;
    private String type;
    private String status;

}
