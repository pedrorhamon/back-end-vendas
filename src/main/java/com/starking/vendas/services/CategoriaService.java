package com.starking.vendas.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.request.CategoriaRequest;
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
    public Categoria criar(CategoriaRequest categoriaRequest) {
        Categoria categoriaNew = new Categoria();
        categoriaNew.setName(categoriaRequest.getName());
        categoriaNew.setCreatedAt(LocalDateTime.now());

        return this.repository.save(categoriaNew);
    }
	
	public Categoria atualizar(Long id, CategoriaRequest categoriaRequest) {
		return repository.findById(id).map(categoriaExistente -> {
			categoriaExistente.setName(categoriaRequest.getName());
			categoriaExistente.setUpdatedAt(LocalDateTime.now());
			return repository.save(categoriaExistente);
		}).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
	}

}
