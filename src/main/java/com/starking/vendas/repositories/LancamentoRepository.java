package com.starking.vendas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Lancamento;

/**
 * @author pedroRhamon
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
