package com.starking.vendas.resource;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.model.response.PessoaResponse;
import com.starking.vendas.services.PessoaService;

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
	

}
