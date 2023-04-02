package com.efigueredo.service_identidate.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Boolean existsByUsername(String username);

    Optional<Usuario> findByUsername(String username);
}
