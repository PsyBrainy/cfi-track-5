package com.alkywallet.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String dni,
        String email,
        LocalDateTime createdAt
) {
}