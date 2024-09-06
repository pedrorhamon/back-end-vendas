package com.starking.vendas.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	public CategoriaResponse criar(CategoriaRequest categoriaRequest) throws IOException {
		Categoria categoria = new Categoria();
		categoria.setName(categoriaRequest.getName());
		categoria.setCreatedAt(LocalDateTime.now());
		
		if (categoriaRequest.getImageFile() != null && !categoriaRequest.getImageFile().isEmpty()) {
			String imageUrl = uploadImage(categoriaRequest.getImageFile());
			categoria.setImageUrl(imageUrl);
		}

		Categoria categoriaSalva = this.repository.save(categoria);

		return new CategoriaResponse(categoriaSalva);
	}
	
	private String uploadImage(MultipartFile imageFile) throws IOException {
		String directory = "/path/to/images/";
	    String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
	    Path filePath = Paths.get(directory + fileName);

	    Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	    // Retorne a URL da imagem (pode ser uma URL local ou externa)
	    return "http://localhost:8080/images/" + fileName;
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
