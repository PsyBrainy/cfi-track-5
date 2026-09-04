package com.alkywallet.repository;

import com.alkywallet.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    
    // Agregamos la búsqueda por email y por dni
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
}