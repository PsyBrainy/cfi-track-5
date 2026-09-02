package com.alkywallet.service;

import com.alkywallet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // Edge Case: Buscar usuario con ID inexistente
    @Test
    void findById_WhenUserDoesNotExist_ShouldThrowException() {
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.obtenerPorId(nonExistentId));
    }

    // Edge Case: Eliminar usuario con ID inexistente
    @Test
    void deleteById_WhenUserDoesNotExist_ShouldThrowException() {
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.eliminar(nonExistentId));
    }
}