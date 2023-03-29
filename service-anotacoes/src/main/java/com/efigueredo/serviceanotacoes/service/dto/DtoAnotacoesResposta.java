package com.efigueredo.serviceanotacoes.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoAnotacoesResposta {

    private Long id;
    private String titulo;
    private String conteudo;
    private LocalDateTime momento;

}
