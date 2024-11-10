package com.starking.vendas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Usuario;

/**
 * @author pedroRhamon
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);

	boolean existsByEmail(String email);


}
