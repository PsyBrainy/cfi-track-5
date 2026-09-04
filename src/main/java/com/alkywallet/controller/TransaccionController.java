package com.alkywallet.controller;

import com.alkywallet.dto.GastoPorTipoDTO;
import com.alkywallet.dto.TransaccionDTO;
import com.alkywallet.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping("/historial")
    public ResponseEntity<List<TransaccionDTO>> getHistorial(Authentication authentication) {
        String email = authentication.getName();
        List<TransaccionDTO> historial = transaccionService.obtenerHistorialPorEmail(email);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/reporte-gastos")
    public ResponseEntity<List<GastoPorTipoDTO>> getReporteGastos(Authentication authentication) {
        String email = authentication.getName();
        List<GastoPorTipoDTO> reporte = transaccionService.obtenerReporteGastosPorEmail(email);
        return ResponseEntity.ok(reporte);
    }
}