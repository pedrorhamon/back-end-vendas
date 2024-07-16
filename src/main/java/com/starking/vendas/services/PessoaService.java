package com.starking.vendas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.request.PessoaRequest;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.model.response.PessoaResponse;
import com.starking.vendas.repositories.PessoaRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class PessoaService {
	
	private final PessoaRepository pessoaRepository;
	
	public List<PessoaResponse> listarTodos() {
	    return this.pessoaRepository.findAll().stream()
	        .map(PessoaResponse::new) 
	        .collect(Collectors.toList());
	}
	
	@Transactional
	public PessoaResponse criar(PessoaRequest pessoaRequest) {
		Pessoa pessoa = new Pessoa();
		pessoa.setName(pessoaRequest.getName());
		pessoa.setAtivo(pessoaRequest.getAtivo());
		pessoa.setCreatedAt(LocalDateTime.now());

		Pessoa pessoaSalva = this.pessoaRepository.save(pessoa);

		return new PessoaResponse(pessoaSalva);
	}

}
