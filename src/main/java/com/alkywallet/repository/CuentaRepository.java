package com.alkywallet.repository;


import com.alkywallet.entity.Cuenta;
import com.alkywallet.entity.TipoMoneda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByUsuarioId(Long usuarioId);
    Optional<Cuenta> findByUsuarioIdAndTipoMoneda(Long usuarioId, TipoMoneda tipoMoneda);
}
