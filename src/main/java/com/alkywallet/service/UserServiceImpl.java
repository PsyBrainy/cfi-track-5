package com.alkywallet.service;

import com.alkywallet.dto.RegisterRequest;
import com.alkywallet.dto.UserRequestDTO;
import com.alkywallet.dto.UserResponseDTO;
import com.alkywallet.dto.UserUpdateDTO;
import com.alkywallet.entity.Cuenta;
import com.alkywallet.entity.Role;
import com.alkywallet.entity.TipoMoneda;
import com.alkywallet.entity.Usuario;
import com.alkywallet.exception.ResourceNotFoundException;
import com.alkywallet.repository.CuentaRepository;
import com.alkywallet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO registrarUsuario(RegisterRequest request) {
        validarUnicidadEmailYDni(request.email(), request.dni(), null);

        Usuario usuario = Usuario.builder()
                .dni(request.dni())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(Role.CLIENT)
                .isDeleted(false)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        crearCuentaInicial(guardado, TipoMoneda.ARS);

        return toResponseDTO(guardado);
    }

    @Override
    public UserResponseDTO crear(UserRequestDTO request) {
        validarUnicidadEmailYDni(request.email(), request.dni(), null);

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .dni(request.dni())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .rol(Role.CLIENT)
                .isDeleted(false)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        crearCuentaInicial(guardado, TipoMoneda.ARS);

        return toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO obtenerPorId(Long id) {
        return toResponseDTO(buscarUsuarioActivo(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .filter(u -> !u.isDeleted())
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public UserResponseDTO actualizar(Long id, UserUpdateDTO request) {
        Usuario usuario = buscarUsuarioActivo(id);

        if (request.nombre() != null && !request.nombre().isBlank()) {
            usuario.setNombre(request.nombre());
        }
        if (request.apellido() != null && !request.apellido().isBlank()) {
            usuario.setApellido(request.apellido());
        }
        if (request.email() != null && !request.email().isBlank()) {
            validarUnicidadEmailYDni(request.email().trim(), null, id);
            usuario.setEmail(request.email().trim());
        }

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
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

    private void validarUnicidadEmailYDni(String email, String dni, Long excludeUserId) {
        if (email != null) {
            usuarioRepository.findByEmail(email)
                    .filter(u -> excludeUserId == null || !u.getId().equals(excludeUserId))
                    .ifPresent(u -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
                    });
        }
        if (dni != null) {
            usuarioRepository.findByDni(dni)
                    .filter(u -> excludeUserId == null || !u.getId().equals(excludeUserId))
                    .ifPresent(u -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "El DNI ya esta registrado");
                    });
        }
    }

    private void crearCuentaInicial(Usuario usuario, TipoMoneda moneda) {
        Cuenta cuenta = Cuenta.builder()
                .usuario(usuario)
                .saldo(BigDecimal.ZERO)
                .tipoMoneda(moneda)
                .isDeleted(false)
                .build();
        cuentaRepository.save(cuenta);
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