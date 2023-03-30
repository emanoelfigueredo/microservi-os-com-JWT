package com.efigueredo.serviceanotacoes.service.dto.requisicao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoAnotacoesCadastroRequisicao {

    private Long id;

    @NotBlank(message = "Campo 'titulo' incorreto")
    @Size(max = 50, message = "Campo 'titulo' deve ter no maximo 50 caracteres")
    private String titulo;

    @NotBlank(message = "Campo 'conteudo' incorreto")
    @Size(max = 2000, message = "Campo 'conteudo' deve ter no maximo 2000 caracteres")
    private String conteudo;

}
