package com.starking.vendas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.repositories.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
	
	@Transactional
	public void criar(Categoria categoria) {
		Categoria categoriaNew = new Categoria();
		categoriaNew.setName(categoria.getName());
		categoriaNew.setCreatedAt(LocalDateTime.now());
		
		this.repository.save(categoriaNew);
	}
	
	public void atualizar(Long id, Categoria categoriaAtualizada) {
		repository.findById(id).map(categoriaExistente -> {
			categoriaExistente.setName(categoriaAtualizada.getName());
			categoriaExistente.setUpdatedAt(LocalDateTime.now());
			return repository.save(categoriaExistente);
		}).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
	}

}
