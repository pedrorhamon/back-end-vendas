package com.starking.vendas.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.request.CategoriaRequest;
import com.starking.vendas.model.response.CategoriaResponse;
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
	public CategoriaResponse criar(CategoriaRequest categoriaRequest) {
		Categoria categoria = new Categoria();
		categoria.setName(categoriaRequest.getName());
		categoria.setCreatedAt(LocalDateTime.now());

		Categoria categoriaSalva = this.repository.save(categoria);

		return new CategoriaResponse(categoriaSalva);
	}
	
	public CategoriaResponse atualizar(Long id, CategoriaRequest categoriaRequest) {
		return repository.findById(id).map(categoriaExistente -> {
			categoriaExistente.setName(categoriaRequest.getName());
			categoriaExistente.setUpdatedAt(LocalDateTime.now());
			return new CategoriaResponse(repository.save(categoriaExistente));
		}).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
	}

}
