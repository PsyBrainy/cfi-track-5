package com.alkywallet.service;

import com.alkywallet.dto.LoginRequest;
import com.alkywallet.dto.LoginResponse;
import com.alkywallet.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Contiene la lógica de negocio de autenticación:
 * valida credenciales contra la base de datos y genera el token JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        // Delega en Spring Security la verificación real de la contraseña
        // (usa el PasswordEncoder y el UserDetailsService configurados).
        // Si las credenciales son inválidas, lanza BadCredentialsException automáticamente.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Si llegamos acá, las credenciales son válidas: buscamos el usuario
        // para generar el token con sus datos/roles actualizados.
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }
}