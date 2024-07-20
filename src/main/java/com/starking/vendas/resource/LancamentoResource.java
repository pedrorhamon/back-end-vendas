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
import com.starking.vendas.model.request.LancamentoRequest;
import com.starking.vendas.model.response.LancamentoResponse;
import com.starking.vendas.services.LancamentoService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class LancamentoResource extends ApiLancamentoBaseControle{
	
	private final LancamentoService lancamentoService;
	
	private final ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<Page<LancamentoResponse>> listar(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String descricao, 
			@PageableDefault(size = 10) Pageable pageable) {

		Page<LancamentoResponse> lancamentos;

		if (id != null && descricao != null) {
			lancamentos = lancamentoService.listarPorIdEDescricao(id, descricao, pageable);
		} else if (id != null) {
			lancamentos = lancamentoService.listarPorId(id, pageable);
		} else if (descricao != null) {
			lancamentos = lancamentoService.listarPorDescricao(descricao, pageable);
		} else {
			lancamentos = lancamentoService.listarTodos(pageable);
		}
		return !lancamentos.isEmpty() ? ResponseEntity.ok(lancamentos)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
	}
	
	@PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody LancamentoRequest lancamentoRequest, HttpServletResponse response) {
        try {
        	LancamentoResponse lancamentoNew = this.lancamentoService.criar(lancamentoRequest);
            
            publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoNew.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoNew);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar o lancamento.");
        }
    }

}
