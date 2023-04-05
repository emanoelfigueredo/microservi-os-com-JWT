package com.efigueredo.serviceanotacoes.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ms-identidade")
public interface UsuariosClient {

    @RequestMapping(method = RequestMethod.GET, value = "/identidade/teste")
    void testeFeign();

}
