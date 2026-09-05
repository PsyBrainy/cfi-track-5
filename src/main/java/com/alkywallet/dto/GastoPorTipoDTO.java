package com.alkywallet.dto;

import com.alkywallet.entity.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoPorTipoDTO {
    private TipoTransaccion tipoTransaccion;
    private BigDecimal total;
}
