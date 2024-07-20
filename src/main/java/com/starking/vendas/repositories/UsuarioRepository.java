package com.starking.vendas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.starking.vendas.model.Usuario;

/**
 * @author pedroRhamon
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.permissoes WHERE u.name = :username")
    Usuario findByNameFetchPermissoes(@Param("username") String username);


}
