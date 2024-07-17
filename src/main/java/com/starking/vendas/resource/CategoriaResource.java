package com.starking.vendas.resource;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.event.RecursoCriadoEvent;
import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.request.CategoriaRequest;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.services.CategoriaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class CategoriaResource extends ApiCategoriaBaseControle{
	
	private final CategoriaService categoriaService;
	
	private final ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Categoria> categorias = this.categoriaService.lista();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma categoria encontrada.");
	}
	
	@PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody CategoriaRequest categoriaRequest, HttpServletResponse response) {
        try {
            CategoriaResponse categoriaNew = this.categoriaService.criar(categoriaRequest);
            
            publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaNew.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaNew);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a categoria.");
        }
    }
	
	
	@PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@Valid @PathVariable Long id, @RequestBody CategoriaRequest categoriaRequest) {
        try {
            CategoriaResponse categoriaAtualizada = this.categoriaService.atualizar(id, categoriaRequest);
            return ResponseEntity.ok(categoriaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar a categoria.");
        }
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarPessoa(@PathVariable Long id) {
		this.categoriaService.deletarCategoria(id);
		return ResponseEntity.noContent().build();
	}

}
