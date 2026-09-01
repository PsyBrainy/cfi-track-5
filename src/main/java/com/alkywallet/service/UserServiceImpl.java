package com.alkywallet.service;

import com.alkywallet.dto.UserRequestDTO;
import com.alkywallet.dto.UserResponseDTO;
import com.alkywallet.dto.UserUpdateDTO;
import com.alkywallet.entity.Usuario;
import com.alkywallet.exception.ResourceNotFoundException;
import com.alkywallet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO crear(UserRequestDTO request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }
        if (usuarioRepository.findByDni(request.dni()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .dni(request.dni())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isDeleted(false)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return toResponseDTO(guardado);
    }

    @Override
    public UserResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarUsuarioActivo(id));
    }

    @Override
    public List<UserResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .filter(u -> !u.isDeleted())
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO actualizar(Long id, UserUpdateDTO request) {
        Usuario usuario = buscarUsuarioActivo(id);

        if (request.nombre() != null && !request.nombre().isBlank()) {
            usuario.setNombre(request.nombre());
        }
        if (request.apellido() != null && !request.apellido().isBlank()) {
            usuario.setApellido(request.apellido());
        }
        if (request.email() != null && !request.email().isBlank()) {
            usuarioRepository.findByEmail(request.email())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está en uso");
                    });
            usuario.setEmail(request.email());
        }

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = buscarUsuarioActivo(id);
        usuario.setDeleted(true);
        usuarioRepository.save(usuario);
    }

    private Usuario buscarUsuarioActivo(Long id) {
        return usuarioRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private UserResponseDTO toResponseDTO(Usuario usuario) {
        return new UserResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getEmail(),
                usuario.getCreatedAt()
        );
    }
}