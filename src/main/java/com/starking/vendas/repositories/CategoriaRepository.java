package com.starking.vendas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Categoria;

/**
 * @author pedroRhamon
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

	List<Categoria> findByNameIn(List<String> categoriaNomes);

}
