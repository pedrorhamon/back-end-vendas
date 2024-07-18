package com.starking.vendas.resource;

import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.services.LancamentoService;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class LancamentoResource extends ApiLancamentoBaseControle{
	
	private final LancamentoService lancamentoService;

}
