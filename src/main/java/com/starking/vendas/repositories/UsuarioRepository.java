package com.starking.vendas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Usuario;

/**
 * @author pedroRhamon
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
