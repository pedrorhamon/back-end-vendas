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
	
//	@Transactional
//	public CategoriaResponse criar(CategoriaRequest categoriaRequest) throws IOException {
//		Categoria categoria = new Categoria();
//		categoria.setName(categoriaRequest.getName());
//		categoria.setCreatedAt(LocalDateTime.now());
//
////		if (categoriaRequest.getImageFile() != null && !categoriaRequest.getImageFile().isEmpty()) {
//			String imageUrl = uploadImage(categoriaRequest.getImageFile());
//			categoria.setImageFile(imageUrl);
////		}
//
//		Categoria categoriaSalva = this.repository.save(categoria);
//
//		return new CategoriaResponse(categoriaSalva);
//	}

	@Transactional
	public CategoriaResponse criar(String name, MultipartFile imageFile) throws IOException {
		Categoria categoria = new Categoria();
		categoria.setName(name);
		categoria.setCreatedAt(LocalDateTime.now());

		// Se o arquivo de imagem foi enviado, armazene-o
		if (imageFile != null && !imageFile.isEmpty()) {
			categoria.setImageFile(imageFile.getBytes());
		} else {
			throw new IllegalArgumentException("Arquivo de imagem é obrigatório.");
		}

		Categoria categoriaSalva = repository.save(categoria);
		return new CategoriaResponse(categoriaSalva);
	}

	private String uploadImage(MultipartFile imageFile) throws IOException {
		// Diretório onde os arquivos serão salvos
		String directory = "E:/dev/projetos/vendas/back-end-vendas/images/";

		// Verifica se o diretório existe, e se não, cria ele
		Path dirPath = Paths.get(directory);
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath); // Cria o diretório se não existir
		}

		// Gera um nome de arquivo único para evitar colisões de nomes
		String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
		Path filePath = dirPath.resolve(fileName);

		// Salva o arquivo no diretório especificado
		Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		// Retorna a URL que pode ser usada para acessar o arquivo posteriormente
		return "http://localhost:8080/images/" + fileName;
	}

	@Transactional
	public CategoriaResponse atualizar(Long id, String name, MultipartFile imageFile) throws IOException {
		Categoria categoriaExistente = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

		// Atualiza o nome da categoria
		categoriaExistente.setName(name);
		categoriaExistente.setUpdatedAt(LocalDateTime.now());

		// Se houver uma nova imagem, atualiza o campo de bytes
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

		// Retorna o array de bytes da imagem
		return categoria.getImageFile();
	}

}
