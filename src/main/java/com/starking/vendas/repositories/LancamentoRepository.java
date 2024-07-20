package com.starking.vendas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.response.LancamentoResponse;

/**
 * @author pedroRhamon
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	Page<Lancamento> findAll(Pageable pageable);

	Page<LancamentoResponse> findById(Long id, Pageable pageable);

}
