package com.efigueredo.service_identidade.infra.conf.security;

import com.efigueredo.service_identidade.domain.Usuario;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class CustomUserDetails implements UserDetails {

    private String username;
    private String senha;
    private Boolean isActive;
    private List<GrantedAuthority> authorities;

    public CustomUserDetails(Usuario usuario) {
        this.username = usuario.getUsername();
        this.senha = usuario.getSenha();
        this.isActive = usuario.getActive();
        this.authorities = Arrays.stream(usuario.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
