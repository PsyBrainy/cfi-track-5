package com.alkywallet.service;

import com.alkywallet.dto.CuentaDTO;
import com.alkywallet.entity.Cuenta;
import com.alkywallet.entity.TipoMoneda;
import com.alkywallet.entity.Usuario;
import com.alkywallet.repository.CuentaRepository;
import com.alkywallet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CuentaDTO obtenerBalancePorUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Cuenta cuenta = cuentaRepository.findByUsuarioIdAndTipoMoneda(usuario.getId(), TipoMoneda.ARS)
                .orElseGet(() -> crearCuentaInicial(usuario, TipoMoneda.ARS));

        return CuentaDTO.builder()
                .id(cuenta.getId())
                .saldo(cuenta.getSaldo())
                .tipoMoneda(cuenta.getTipoMoneda())
                .build();
    }

    @Transactional
    public CuentaDTO obtenerBalancePorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Cuenta cuenta = cuentaRepository.findByUsuarioIdAndTipoMoneda(usuario.getId(), TipoMoneda.ARS)
                .orElseGet(() -> crearCuentaInicial(usuario, TipoMoneda.ARS));

        return CuentaDTO.builder()
                .id(cuenta.getId())
                .saldo(cuenta.getSaldo())
                .tipoMoneda(cuenta.getTipoMoneda())
                .build();
    }

    private Cuenta crearCuentaInicial(Usuario usuario, TipoMoneda tipoMoneda) {
        Cuenta nuevaCuenta = Cuenta.builder()
                .usuario(usuario)
                .saldo(BigDecimal.ZERO)
                .tipoMoneda(tipoMoneda)
                .isDeleted(false)
                .build();

        return cuentaRepository.save(nuevaCuenta);
    }
}
