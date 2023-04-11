package com.efigueredo.serviceanotacoes.service.roles;

public enum Roles {

    ADMINISTRADOR {

        @Override
        public boolean corresponde(String valor) {
            if(valor.equals("ROLE_ADMINISTRADOR,ROLE_USUARIO")) {
                return true;
            }
            return false;
        }

    },
    USUARIO {

        @Override
        public boolean corresponde(String valor) {
            if(valor.equals("ROLE_ADMINISTRADOR,ROLE_USUARIO") || valor.equals("ROLE_USUARIO")) {
                return true;
            }
            return false;
        }

    };

    public abstract boolean corresponde(String valor);

}
