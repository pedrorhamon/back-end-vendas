package com.starking.vendas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.response.LancamentoResponse;
import com.starking.vendas.repositories.LancamentoRepository;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class LancamentoService {
	
	private final LancamentoRepository lancamentoRepository;
	
	public List<LancamentoResponse> listarTodos() {
	    return this.lancamentoRepository.findAll().stream()
	        .map(LancamentoResponse::new) 
	        .collect(Collectors.toList());
	}

}
