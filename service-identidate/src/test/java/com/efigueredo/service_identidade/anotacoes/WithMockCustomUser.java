package com.efigueredo.service_identidade.anotacoes;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String nome() default "Emanoel";

    String username() default "efigueredo";

    String senha() default "Rob Winch";

    String roles() default "ROLE_USUARIO";

}