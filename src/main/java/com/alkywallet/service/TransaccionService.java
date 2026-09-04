package com.alkywallet.service;

import com.alkywallet.exception.ResourceNotFoundException;
import com.alkywallet.exception.SaldoInsuficienteException;
import com.alkywallet.entity.Cuenta;
import com.alkywallet.entity.TipoTransaccion;
import com.alkywallet.entity.Transaccion;
import com.alkywallet.repository.CuentaRepository;
import com.alkywallet.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransaccionService {
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    @Transactional
    public void realizarDeposito(Long cuentaId, Double monto) {
        if (cuentaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID de la cuenta es obligatorio");
        }
        if (monto == null || monto <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero");
        }

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        if (cuenta.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuenta destino inactiva");
        }

        BigDecimal montoBigDecimal = BigDecimal.valueOf(monto);
        BigDecimal nuevoSaldo = cuenta.getSaldo().add(montoBigDecimal);
        cuenta.setSaldo(nuevoSaldo);

        cuentaRepository.save(cuenta);

        Transaccion transaccion = Transaccion.builder()
                .monto(montoBigDecimal)
                .fecha(LocalDateTime.now())
                .tipo(TipoTransaccion.DEPOSITO)
                .concepto("Deposito en cuenta")
                .cuenta(cuenta)
                .build();

        transaccionRepository.save(transaccion);
    }
        @Transactional
    public void realizarTransferencia(Long cuentaOrigenId, Long cuentaDestinoId, Double monto) {
        if (cuentaOrigenId == null || cuentaDestinoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las cuentas de origen y destino son obligatorias");
        }
        if (cuentaOrigenId.equals(cuentaDestinoId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cuenta destino debe ser distinta a la cuenta origen");
        }
        if (monto == null || monto <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a cero");
        }

        Cuenta cuentaOrigen = cuentaRepository.findById(cuentaOrigenId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta origen no encontrada"));

        Cuenta cuentaDestino = cuentaRepository.findById(cuentaDestinoId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta destino no encontrada"));

        if (cuentaOrigen.isDeleted() || cuentaDestino.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede transferir desde o hacia una cuenta inactiva");
        }

        BigDecimal montoBigDecimal = BigDecimal.valueOf(monto);

        if (cuentaOrigen.getSaldo().compareTo(montoBigDecimal) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta origen");
        }

        // --- Débito en cuenta origen ---
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(montoBigDecimal));
        cuentaRepository.save(cuentaOrigen);

        Transaccion egreso = Transaccion.builder()
                .monto(montoBigDecimal)
                .fecha(LocalDateTime.now())
                .tipo(TipoTransaccion.EGRESO)
                .concepto("Transferencia a cuenta " + cuentaDestinoId)
                .cuenta(cuentaOrigen)
                .build();
        transaccionRepository.save(egreso);

        // --- Crédito en cuenta destino ---
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(montoBigDecimal));
        cuentaRepository.save(cuentaDestino);

        Transaccion ingreso = Transaccion.builder()
                .monto(montoBigDecimal)
                .fecha(LocalDateTime.now())
                .tipo(TipoTransaccion.INGRESO)
                .concepto("Transferencia desde cuenta " + cuentaOrigenId)
                .cuenta(cuentaDestino)
                .build();
        transaccionRepository.save(ingreso);
    }
}
