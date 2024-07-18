package com.starking.vendas.services;

import org.springframework.stereotype.Service;

import com.starking.vendas.repositories.LancamentoRepository;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class LancamentoService {
	
	private final LancamentoRepository lancamentoRepository;

}
