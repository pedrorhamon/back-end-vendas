package com.starking.vendas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.Permissao;

/**
 * @author pedroRhamon
 */
public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

	Permissao findByName(String permissaoName);

}
