package com.starking.vendas.resource;

import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.services.CategoriaService;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class CategoriaResource extends ApiBaseControle{
	
	private final CategoriaService categoriaService;

}
