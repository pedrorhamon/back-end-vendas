package com.starking.vendas.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author pedroRhamon
 */
@Entity
@Table(name = "categoria")
@Data
public class Categoria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime createdAt;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime updatedAt;

}
