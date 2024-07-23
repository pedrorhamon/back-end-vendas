package com.starking.vendas.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Pessoa;

/**
 * @author pedroRhamon
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	Page<Pessoa> findById(Long id, Pageable pageable);
	
	Page<Pessoa> findByName(String name, Pageable pageable);

}
