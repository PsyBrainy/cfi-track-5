package com.alkywallet.entity;

public enum TipoTransaccion {
    DEPOSITO(true, "Deposito"),
    INGRESO(true, "Transferencia Recibida"),
    EGRESO(false, "Transferencia Enviada");

    private final boolean ingreso;
    private final String descripcion;

    TipoTransaccion(boolean ingreso, String descripcion) {
        this.ingreso = ingreso;
        this.descripcion = descripcion;
    }

    public boolean isIngreso() {
        return ingreso;
    }

    public String getDescripcion() {
        return descripcion;
    }
}