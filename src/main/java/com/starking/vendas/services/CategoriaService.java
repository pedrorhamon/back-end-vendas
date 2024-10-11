package com.starking.vendas.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Base64;
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
	public CategoriaResponse criar(String name, MultipartFile imageFile) throws IOException {
		Categoria categoria = new Categoria();
		categoria.setName(name);
		categoria.setCreatedAt(LocalDateTime.now());

		if (imageFile != null && !imageFile.isEmpty()) {
			categoria.setImageFile(imageFile.getBytes());
		} else {
			throw new IllegalArgumentException("Arquivo de imagem é obrigatório.");
		}

		Categoria categoriaSalva = repository.save(categoria);
		return new CategoriaResponse(categoriaSalva);
	}

	private String uploadImage(MultipartFile imageFile) throws IOException {
		String directory = "E:/dev/projetos/vendas/back-end-vendas/images/";

		Path dirPath = Paths.get(directory);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath); // Cria o diretório se não existir
		}
		String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
		Path filePath = dirPath.resolve(fileName);

		Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		return "http://localhost:8080/images/" + fileName;
	}

	@Transactional
	public CategoriaResponse atualizar(Long id, String name, MultipartFile imageFile) throws IOException {
		Categoria categoriaExistente = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

		categoriaExistente.setName(name);
		categoriaExistente.setUpdatedAt(LocalDateTime.now());

		if (imageFile != null && !imageFile.isEmpty()) {
			categoriaExistente.setImageFile(Base64.getEncoder().encodeToString(categoriaExistente.getImageFile()).getBytes());
		}

		Categoria categoriaAtualizada = repository.save(categoriaExistente);
		return new CategoriaResponse(categoriaAtualizada);
	}


	@Transactional
    public void deletarCategoria(Long id) {
        Categoria categoria = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
        repository.delete(categoria);
    }

	public byte[] obterImagemPorId(Long id) {
		Categoria categoria = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

		return categoria.getImageFile();
	}

}
