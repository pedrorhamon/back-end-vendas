package com.starking.vendas.resource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.event.RecursoCriadoEvent;
import com.starking.vendas.model.request.CredenciaisRequest;
import com.starking.vendas.model.request.UsuarioRequest;
import com.starking.vendas.model.response.TokenResponse;
import com.starking.vendas.model.response.UsuarioResponse;
import com.starking.vendas.resource.apis_base.ApiUsuarioBaseControle;
import com.starking.vendas.services.JwtService;
import com.starking.vendas.services.UsuarioService;

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
public class UsuarioResource extends ApiUsuarioBaseControle{
	
	private final UsuarioService usuarioService;
	
	private final JwtService jwtService;
	
	private final ApplicationEventPublisher publisher;
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar( @RequestBody @Valid CredenciaisRequest request ) {
		try {
			UsuarioResponse usuarioAutenticado = this.usuarioService.autenticar(request.getEmail(), request.getSenha(), request.getRecaptchaResponse());
			String token = jwtService.gerarToken(usuarioAutenticado);
			TokenResponse tokenDTO = new TokenResponse( usuarioAutenticado.getName(), token);
			return ResponseEntity.ok(tokenDTO);
		}catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponse> obterUsuarioPorId(@PathVariable Long id) {
		UsuarioResponse usuarioResponse = usuarioService.obterUsuarioPorId(id);
		return ResponseEntity.ok(usuarioResponse);
	}
	
	@GetMapping
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<Page<UsuarioResponse>> listar(@PageableDefault(size = 10) Pageable pageable) {
		Page<UsuarioResponse> usuarios = usuarioService.listarTodos(pageable);
		return usuarios.isEmpty() ? ResponseEntity.ok(Page.empty(pageable)) : ResponseEntity.ok(usuarios);
	}

	@PostMapping
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
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
	
	@PutMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
    public ResponseEntity<?> atualizar(@Valid @PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest) {
        try {
        	UsuarioResponse usuarioAtualizado = this.usuarioService.atualizarUsuario(id, usuarioRequest);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar a Usuário.");
        }
    }
	
	@PutMapping("/desativar/{id}")
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<?> desativar(@PathVariable Long id) {
		usuarioService.desativar(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
		usuarioService.excluirUsuario(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        jwtService.revokeToken(token);
        return ResponseEntity.ok().build();
    }
	
	@PostMapping("/esquecer-senha")
    public ResponseEntity<String> esquecerSenha(@RequestParam	 String email) {
        usuarioService.esquecerSenha(email);
        return ResponseEntity.ok("Senha enviada para o email fornecido");
    }
}
