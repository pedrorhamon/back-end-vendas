package com.starking.vendas.resource;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.event.RecursoCriadoEvent;
import com.starking.vendas.model.request.PessoaRequest;
import com.starking.vendas.model.response.PessoaResponse;
import com.starking.vendas.resource.apis_base.ApiPessoaBaseControle;
import com.starking.vendas.services.PessoaService;

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
public class PessoaResource extends ApiPessoaBaseControle{
	
	private final PessoaService pessoaService;
	
	private final ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<Page<PessoaResponse>> listar(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String name, 
			@PageableDefault(size = 10) Pageable pageable) {
		Page<PessoaResponse> pessoas; 
		
		if (id != null) {
			pessoas = pessoaService.listarPorId(id, pageable);
		} else if (name != null) {
			pessoas = pessoaService.listarPorName(name, pageable);
		} else {
			pessoas = this.pessoaService.listarTodos(pageable);
		}
		
		return !pessoas.isEmpty() ? ResponseEntity.ok(pessoas) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
    public ResponseEntity<?> criar(@Valid @RequestBody PessoaRequest pessoaRequest, HttpServletResponse response) {
        try {
            PessoaResponse pessoaNew = this.pessoaService.criar(pessoaRequest);
            
            publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaNew.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(pessoaNew);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a pessoa.");
        }
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
    public ResponseEntity<?> atualizar(@Valid @PathVariable Long id, @RequestBody PessoaRequest pessoaRequest) {
        try {
        	PessoaResponse pessoaAtualizada = this.pessoaService.atualizar(id, pessoaRequest);
            return ResponseEntity.ok(pessoaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar a categoria.");
        }
    }
	
	@PutMapping("/desativar/{id}")
	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<?> desativar(@PathVariable Long id) {
		 this.pessoaService.desativar(id);
		 return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
    public ResponseEntity<?> deletarPessoa(@PathVariable Long id) {
        pessoaService.deletarPessoa(id);
        return ResponseEntity.noContent().build();
    }
	

}
