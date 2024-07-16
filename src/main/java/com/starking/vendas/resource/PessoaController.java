package com.starking.vendas.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.model.request.PessoaRequest;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.model.response.PessoaResponse;
import com.starking.vendas.services.PessoaService;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class PessoaController extends ApiPessoaBaseControle{
	
	private final PessoaService pessoaService;
	
	@GetMapping
	public ResponseEntity<?> listar() {
		List<PessoaResponse> pessoas = this.pessoaService.listarTodos();
		return !pessoas.isEmpty() ? ResponseEntity.ok(pessoas) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma pessoa encontrada.");
	}
	
	@PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PessoaRequest pessoaRequest) {
        try {
            PessoaResponse pessoaNew = this.pessoaService.criar(pessoaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(pessoaNew);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a pessoa.");
        }
    }
	

}
