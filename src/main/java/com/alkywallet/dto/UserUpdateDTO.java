package com.alkywallet.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateDTO(
        String nombre,
        String apellido,

        @Email(message = "El email debe tener un formato válido")
        String email
) {
}