package com.alkywallet.dto;

import com.alkywallet.entity.TipoMoneda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaDTO {
    private Long id;
    private BigDecimal saldo;
    private TipoMoneda tipoMoneda;
}
