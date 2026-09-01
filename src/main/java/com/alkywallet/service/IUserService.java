package com.alkywallet.service;

import com.alkywallet.dto.UserRequestDTO;
import com.alkywallet.dto.UserResponseDTO;
import com.alkywallet.dto.UserUpdateDTO;

import java.util.List;

public interface IUserService {

    UserResponseDTO crear(UserRequestDTO request);

    UserResponseDTO obtenerPorId(Long id);

    List<UserResponseDTO> obtenerTodos();

    UserResponseDTO actualizar(Long id, UserUpdateDTO request);

    void eliminar(Long id);
}
