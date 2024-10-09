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
	public CategoriaResponse criar(CategoriaRequest categoriaRequest) throws IOException {
		Categoria categoria = new Categoria();
		categoria.setName(categoriaRequest.getName());
		categoria.setCreatedAt(LocalDateTime.now());

		// Se houver um arquivo de imagem, converte-o para bytes e armazena
		if (categoriaRequest.getImageFile() != null && !categoriaRequest.getImageFile().isEmpty()) {
			categoria.setImageFile(categoriaRequest.getImageFile().getBytes());
		}

		Categoria categoriaSalva = this.repository.save(categoria);

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


//	private String uploadImage(MultipartFile imageFile) throws IOException {
//		String directory = "/path/to/images/";
//	    String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
//	    Path filePath = Paths.get(directory + fileName);
//
//	    Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//	    // Retorne a URL da imagem (pode ser uma URL local ou externa)
//	    return "http://localhost:8080/images/" + fileName;
//	}
	
//	public CategoriaResponse atualizar(Long id, CategoriaRequest categoriaRequest) {
//		return repository.findById(id).map(categoriaExistente -> {
//			categoriaExistente.setName(categoriaRequest.getName());
//			categoriaExistente.setUpdatedAt(LocalDateTime.now());
//
//			if (categoriaRequest.getImageFile() != null && !categoriaRequest.getImageFile().isEmpty()) {
//                String imageUrl = null;
//                try {
//                    imageUrl = uploadImage(categoriaRequest.getImageFile());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                categoriaExistente.setImageUrl(imageUrl);
//			}
//
//			return new CategoriaResponse(repository.save(categoriaExistente));
//		}).orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
//	}

	@Transactional
	public CategoriaResponse atualizar(Long id, CategoriaRequest categoriaRequest) throws IOException {
		Categoria categoriaExistente = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

		categoriaExistente.setName(categoriaRequest.getName());
		categoriaExistente.setUpdatedAt(LocalDateTime.now());

		// Se houver uma nova imagem, atualiza o campo de bytes
		if (categoriaRequest.getImageFile() != null && !categoriaRequest.getImageFile().isEmpty()) {
			categoriaExistente.setImageFile(categoriaRequest.getImageFile().getBytes());
		}

		Categoria categoriaAtualizada = this.repository.save(categoriaExistente);

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
