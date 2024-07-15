package com.starking.vendas.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.repositories.CategoriaRepository;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class CategoriaService {
	
	private final CategoriaRepository repository;
	
	
	public List<Categoria> lista() {
		return this.repository.findAll();
	}

}
