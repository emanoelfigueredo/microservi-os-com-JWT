<<<<<<< HEAD
package com.efigueredo.serviceanotacoes.service.dto;

import com.efigueredo.serviceanotacoes.domain.Anotacao;
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
=======
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
>>>>>>> 53d1cfec92158846490aa39cdc9c0d52f97104f5
