package com.starking.vendas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Usuario;

/**
 * @author pedroRhamon
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

//	@Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.permissoes WHERE u.name = :username")
//    Usuario findByNameFetchPermissoes(@Param("username") String username);

	Optional<Usuario> findByEmail(String email);

	boolean existsByEmail(String email);


}
