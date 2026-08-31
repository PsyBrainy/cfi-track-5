package com.alkywallet.service;

import com.alkywallet.dto.RegisterRequest;
import com.alkywallet.entity.Usuario;
import com.alkywallet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void registrarUsuario(RegisterRequest request) {
        // Validar si el correo ya está registrado (409 Conflict)
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya existe");
        }
        // Construir la entidad con la contraseña encriptada
        Usuario nuevoUsuario = Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isDeleted(false)
                .build();
        // Persistir en la base de datos
        usuarioRepository.save(nuevoUsuario);
    }
}
