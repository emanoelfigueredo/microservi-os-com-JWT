package com.efigueredo.service_identidade.anotacoes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockCustomUser
public @interface User {
}
