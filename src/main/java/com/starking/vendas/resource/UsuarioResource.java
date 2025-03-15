package com.starking.vendas.resource;

import com.starking.vendas.model.DocumentoAssinado;
import com.starking.vendas.model.request.AlterarSenhaRequest;
import com.starking.vendas.services.DocumentoAssinadoService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class UsuarioResource extends ApiUsuarioBaseControle{
	
	private final UsuarioService usuarioService;
	
	private final JwtService jwtService;
	
	private final ApplicationEventPublisher publisher;
	private final DocumentoAssinadoService documentoAssinadoService;

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

	@PostMapping("/{gestorId}/inativar/{usuarioId}")
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<UsuarioResponse> inativarUsuario(@PathVariable Long gestorId, @PathVariable Long usuarioId) {
		UsuarioResponse response = usuarioService.desativar(gestorId, usuarioId);
		return ResponseEntity.ok(response);
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
    public ResponseEntity<String> esquecerSenha(@RequestParam String email) {
        usuarioService.esquecerSenha(email);
        return ResponseEntity.noContent().build();
    }

	@PutMapping("/alterar-senha")
	public ResponseEntity<?> alterarSenha(@RequestBody @Valid AlterarSenhaRequest alterarSenhaRequest,
										  @RequestHeader("Authorization") String token) {
		try {
			if (token.startsWith("Bearer ")) {
				token = token.substring(7);
			}

			usuarioService.alterarSenha(alterarSenhaRequest, token);

			return ResponseEntity.ok("Senha alterada com sucesso.");

		} catch (ValidationException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar a senha.");
		}
	}

	@PostMapping("/{id}/assinar-documento")
	public ResponseEntity<?> assinarDocumento(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
		try {
			DocumentoAssinado documento = documentoAssinadoService.assinarDocumento(id, file);
			return ResponseEntity.ok().body("Documento assinado e armazenado com sucesso. ID: " + documento.getId());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}/documentos")
	public ResponseEntity<?> listarDocumentos(@PathVariable Long id) {
		Optional<DocumentoAssinado> documento = documentoAssinadoService.obterDocumento(id);

		if (documento.isPresent()) {
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=" + documento.get().getNomeArquivo())
					.body(documento.get().getConteudo());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado.");
		}
	}

	@GetMapping("/documento/{documentoId}")
	public ResponseEntity<byte[]> obterDocumento(@PathVariable Long documentoId) {
		Optional<DocumentoAssinado> documento = documentoAssinadoService.obterDocumento(documentoId);
		if (documento.isPresent()) {
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=" + documento.get().getNomeArquivo())
					.body(documento.get().getConteudo());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
