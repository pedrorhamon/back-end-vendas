package com.starking.vendas.model.response;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pedroRhamon
 */

@Getter
@Setter
public class CategoriaResponse {
	
	private Long id;
	
	@NotNull
	@NotBlank
	private String name;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime createdAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime updatedAt;
	
	public CategoriaResponse(Categoria entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
	}

}
