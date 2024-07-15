package com.starking.vendas.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.request.CategoriaRequest;
import com.starking.vendas.services.CategoriaService;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class CategoriaResource extends ApiBaseControle{
	
	private final CategoriaService categoriaService;
	
	
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Categoria> categorias = this.categoriaService.lista();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma categoria encontrada.");
	}
	
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody CategoriaRequest categoriaRequest) {
		try {
			Categoria categoriaNew = this.categoriaService.criar(categoriaRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(categoriaNew);			
		} catch (ValidationException e) {
			 return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
		} catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a categoria.");
		}
	}

}
