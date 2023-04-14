package com.efigueredo.service_identidade.infra.conf.filter;

import com.efigueredo.service_identidade.domain.Usuario;
import com.efigueredo.service_identidade.domain.UsuarioRepository;
import com.efigueredo.service_identidade.infra.conf.security.CustomUserDetails;
import com.efigueredo.service_identidade.service.TokenJwtService;
import com.netflix.discovery.converters.Auto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AutenticacaoFilter extends OncePerRequestFilter {

    @Autowired
    private TokenJwtService tokenJwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJwt = this.recuperarToken(request);
        if(tokenJwt != null) {
            this.tokenJwtService.validarToken(tokenJwt);
            String username = this.tokenJwtService.obterSubject(tokenJwt);
            Usuario usuario = this.usuarioRepository.findByUsername(username).get();
            CustomUserDetails userDetails = new CustomUserDetails(usuario);
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorization = request.getHeader("AUTHORIZATION");
        if(authorization != null) {
            return authorization.replace("Bearer ", "");
        }
        return null;
    }
}
