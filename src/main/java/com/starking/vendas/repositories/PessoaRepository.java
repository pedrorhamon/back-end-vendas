package com.starking.vendas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Pessoa;

/**
 * @author pedroRhamon
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
