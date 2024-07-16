package com.starking.vendas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.response.PessoaResponse;
import com.starking.vendas.repositories.PessoaRepository;

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

}
