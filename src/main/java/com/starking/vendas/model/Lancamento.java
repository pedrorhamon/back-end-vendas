package com.starking.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.enums.TipoLancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author pedroRhamon
 */
@Entity
@Table(name = "lancamento")
@Data
public class Lancamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@NotBlank
	private String descricao;
	
	@Column(name = "data_vencimento")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataVencimento;
	
	@Column(name = "data_pagamento")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataPagamento;
	
	@NotNull
	private BigDecimal valor;
	
	private String observacao;
	
//	@NotNull
//	@ManyToOne
//	@JoinColumn(name = "id_categoria")
//	private Categoria categoria;
//	
//	@NotNull
//	@ManyToOne
//	@JoinColumn(name = "id_pessoa")
//	private Pessoa pessoa;
	
	@ManyToMany
	@JoinTable(
	    name = "lancamento_categoria",
	    joinColumns = @JoinColumn(name = "lancamento_id"),
	    inverseJoinColumns = @JoinColumn(name = "categoria_id"))
	private List<Categoria> categorias;
	
	@ManyToMany
	@JoinTable(
	    name = "lancamento_pessoa",
	    joinColumns = @JoinColumn(name = "lancamento_id"),
	    inverseJoinColumns = @JoinColumn(name = "pessoa_id"))
	private List<Pessoa> pessoas;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo")
	private TipoLancamento tipoLancamento;
	
}
