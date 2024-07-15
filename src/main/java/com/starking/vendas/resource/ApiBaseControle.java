package com.starking.vendas.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starking.vendas.services.CategoriaService;

import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class ApiBaseControle {
	
	private final CategoriaService service;

}
