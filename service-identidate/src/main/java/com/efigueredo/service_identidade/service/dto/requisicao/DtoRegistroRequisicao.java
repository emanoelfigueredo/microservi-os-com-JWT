package com.efigueredo.service_identidade.service.dto.requisicao;

import com.efigueredo.service_identidade.domain.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DtoRegistroRequisicao {

        Long id;

        @NotBlank(message = "Campo nome não deve ser nulo ou vazio")
        @Size(min = 3, max = 50, message = "Campo deve conter no mínimo 3 caracteres de no máximo 50")
        String nome;

        @NotBlank(message = "Campo username não deve ser nulo ou vazio")
        @Size(min = 5, max = 50, message = "Campo deve conter no mínimo 5 caracteres de no máximo 50")
        String username;

        @NotBlank(message = "Campo nome não deve ser nulo ou vazio")
        @Size(min = 8, max = 100, message = "Campo deve conter no mínimo 8 caracteres de no máximo 100")
        String senha;

        public Usuario toUsuario() {
                return new Usuario(id, nome, username, senha);
        }

}
