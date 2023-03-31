package com.efigueredo.service_identidate.infra.controller;

import com.efigueredo.service_identidate.service.dto.requisicao.DtoRegistroRequisicao;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("identidade")
public class IdentidadeController {

    @PostMapping("registrar")
    public void registrar(@RequestBody @Valid DtoRegistroRequisicao dto) {
        System.out.println(dto);
    }

}
