package com.starking.vendas.model.response;

import java.time.format.DateTimeFormatter;

import com.starking.vendas.model.Categoria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pedroRhamon
 */

@Getter
@Setter
@NoArgsConstructor
public class CategoriaResponse {
	
	private Long id;
	
	private String name;
	
	private String createdAt;
	
	private String updatedAt;
	
	public CategoriaResponse(Categoria entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.createdAt = entity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.updatedAt = entity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
