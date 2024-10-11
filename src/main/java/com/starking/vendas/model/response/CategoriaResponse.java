package com.starking.vendas.model.response;

import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pedroRhamon
 */

@Getter
@Setter
public class CategoriaResponse {
	
	private Long id;
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 50)
	private String name;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime createdAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime updatedAt;
	
//	private String imageUrl;

	private String imageFile;

	public CategoriaResponse(Categoria entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
		this.imageFile = entity.getImageFile() != null ? Base64.getEncoder().encodeToString(entity.getImageFile()) : null;
	}

}
