package com.starking.vendas.repositories;

import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.SubPermissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author pedroRhamon
 */
public interface SubPermissaoRepository extends JpaRepository<SubPermissao, Long>{

//	List<SubPermissao> findByPermissaoPrincipal(Permissao permissaoPrincipal);

	Long findById(long id);

}
