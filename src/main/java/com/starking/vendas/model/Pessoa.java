package com.starking.vendas.model;

import com.starking.vendas.model.embedded.Endereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author pedroRhamon
 */
@Entity
@Table(name = "pessoa")
@Data
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private Boolean ativo = true;
	
	@Column(name = "created_at")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
//	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
//	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime updatedAt;
	
	@Embedded
    private Endereco endereco;

	private Point coordenadas;

}
