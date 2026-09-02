package com.alkywallet.service;

import com.alkywallet.dto.RegisterRequest;
import com.alkywallet.entity.Usuario;
import com.alkywallet.exception.ResourceNotFoundException;
import com.alkywallet.repository.CuentaRepository;
import com.alkywallet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registrarUsuario_HappyPath_ShouldSaveUserSuccessfully() {
        RegisterRequest request = new RegisterRequest("12345678", "juan.perez@email.com", "123456");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.findByDni(request.dni())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        userService.registrarUsuario(request);

        verify(userRepository).save(any(Usuario.class));
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldThrowException() {
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.obtenerPorId(nonExistentId));
    }

    @Test
    void deleteById_WhenUserDoesNotExist_ShouldThrowException() {
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.eliminar(nonExistentId));
    }
}