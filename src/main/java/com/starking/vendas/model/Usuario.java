package com.starking.vendas.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author pedroRhamon
 */

@Entity
@Table(name = "usuario")
@Data
public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;
	
	private String senha;

	@ManyToMany
	@JoinTable(
	        name = "usuario_permissoes",
	        joinColumns = @JoinColumn(name = "usuario_id"),
	        inverseJoinColumns = @JoinColumn(name = "permissoes_id")
	    )
	private List<Permissao> permissoes;
}
