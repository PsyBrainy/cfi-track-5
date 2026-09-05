package com.alkywallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoTransaccion {
    DEPOSITO(true, "Depósito"),
    INGRESO(true, "Transferencia Recibida"),
    EGRESO(false, "Transferencia Enviada");

    private final boolean ingreso;
    private final String descripcion;
}