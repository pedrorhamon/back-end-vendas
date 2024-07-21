package com.starking.vendas.resource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.event.RecursoCriadoEvent;
import com.starking.vendas.model.request.UsuarioRequest;
import com.starking.vendas.model.response.UsuarioResponse;
import com.starking.vendas.services.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class UsuarioResource extends ApiUsuarioBaseControle{
	
	private final UsuarioService usuarioService;
	
	private final ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<Page<UsuarioResponse>> listar(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String descricao, 
			@PageableDefault(size = 10) Pageable pageable) {

		Page<UsuarioResponse> usuarios = usuarioService.listarTodos(pageable);
		return !usuarios.isEmpty() ? ResponseEntity.ok(usuarios)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
	}

	@PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody UsuarioRequest usuarioRequest, HttpServletResponse response) {
        try {
        	UsuarioResponse usuarioNew = this.usuarioService.criarUsuario(usuarioRequest);
            
            publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioNew.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNew);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar o Usuário.");
        }
    }
}
