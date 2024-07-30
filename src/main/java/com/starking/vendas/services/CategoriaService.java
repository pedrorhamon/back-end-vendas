package com.starking.vendas.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	
	public Page<CategoriaResponse> lista(Pageable pageable) {
		Page<Categoria> categoriaPage = repository.findAll(pageable);
		 return categoriaPage.map(CategoriaResponse::new);
	}
	
	public CategoriaResponse findById(Long id) {
		Categoria categoria = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria not found"));
		return new CategoriaResponse(categoria);
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
	

    @Transactional
    public void deletarCategoria(Long id) {
        Categoria categoria = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
        repository.delete(categoria);
    }

}
