package com.alkywallet.controller;

import com.alkywallet.dto.CuentaDTO;
import com.alkywallet.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping("/balance")
    public ResponseEntity<CuentaDTO> getBalance(Authentication authentication) {
        String email = authentication.getName();
        CuentaDTO balance = cuentaService.obtenerBalancePorEmail(email);
        return ResponseEntity.ok(balance);
    }
}
