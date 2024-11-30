package com.starking.vendas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pedroRhamon
 */
@Entity
@Table(name = "permissao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permissao implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "\"name\"", nullable = false)
	private String name;

	@ManyToMany
	@JoinTable(
			name = "permissao_sub_permissao",
			joinColumns = @JoinColumn(name = "permissao_id"),
			inverseJoinColumns = @JoinColumn(name = "sub_permissao_id")
	)
	private List<SubPermissao> subPermissoes = new ArrayList<>();
	
	public Permissao(String name) {
        this.name = name;
    }
}
