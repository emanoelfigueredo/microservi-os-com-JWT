package com.efigueredo.service_identidade.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public record DtoAutenticacao(

        @NotBlank(message = "Campo username não deve ser nulo ou vazio")
        @Size(min = 5, max = 50, message = "Campo deve conter no mínimo 5 caracteres de no máximo 50")
        String username,

        @NotBlank(message = "Campo nome não deve ser nulo ou vazio")
        @Size(min = 8, max = 100, message = "Campo deve conter no mínimo 8 caracteres de no máximo 100")
        String senha

) {

}
