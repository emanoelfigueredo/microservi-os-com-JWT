package com.efigueredo.service_identidade.anotacoes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockCustomUser(nome = "administrador", username = "admin", senha = "admin123", roles = "ROLE_ADMINISTRADOR,ROLE_USUARIO")
public @interface Admin {
}
