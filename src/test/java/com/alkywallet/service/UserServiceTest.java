package com.alkywallet.service;

import com.alkywallet.dto.RegisterRequest;
import com.alkywallet.entity.Usuario;
import com.alkywallet.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrarUsuario_HappyPath_ShouldSaveUserSuccessfully() {
        // 1. ARRANGE
        RegisterRequest request = new RegisterRequest("12345678", "juan.perez@email.com", "123456");

        when(usuarioRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(usuarioRepository.findByDni(request.dni())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        // 2. ACT
        usuarioService.registrarUsuario(request);

        // 3. ASSERT
        verify(usuarioRepository).save(any(Usuario.class));
    }
}