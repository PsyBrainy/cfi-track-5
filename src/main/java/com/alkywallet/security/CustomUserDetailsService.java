package com.alkywallet.security;

import com.alkywallet.entity.Usuario;
import com.alkywallet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementación de UserDetailsService: busca al Usuario en la base de datos
 * por email y lo adapta al contrato UserDetails que espera Spring Security.
 */
@Service
@RequiredArgsConstructor
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                Collections.emptyList() // sin roles todavía, tu entidad no tiene ese campo aún
        );
    }
}
