package com.efigueredo.serviceanotacoes.infra.client;

import feign.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ms-identidade")
public interface UsuarioRoleClient {

    @RequestMapping(method = RequestMethod.POST, path = "usuarios/role/{tokenJWT}")
    String obterRolePorTokenJwt(@PathVariable String tokenJWT);

}
