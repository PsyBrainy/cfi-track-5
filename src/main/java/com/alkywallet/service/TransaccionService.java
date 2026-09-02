package com.alkywallet.service;

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
}
