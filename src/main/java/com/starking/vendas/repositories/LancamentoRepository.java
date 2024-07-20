package com.starking.vendas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Lancamento;

/**
 * @author pedroRhamon
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	Page<Lancamento> findAll(Pageable pageable);

	Page<Lancamento> findById(Long id, Pageable pageable);
    Page<Lancamento> findByDescricaoContaining(String descricao, Pageable pageable);
    Page<Lancamento> findByIdAndDescricaoContaining(Long id, String descricao, Pageable pageable);

}
