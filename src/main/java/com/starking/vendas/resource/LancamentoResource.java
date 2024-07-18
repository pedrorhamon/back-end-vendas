package com.starking.vendas.resource;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<?> listar() {
		List<LancamentoResponse> lancamentos = this.lancamentoService.listarTodos();
		return !lancamentos.isEmpty() ? ResponseEntity.ok(lancamentos) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum Lançamento foi encontrado.");
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
