package com.starking.vendas.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.enums.TipoLancamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pedroRhamon
 */

@Getter
@Setter
@AllArgsConstructor
public class LancamentoResponse {
	
	private Long id;
	
	@NotNull
	@NotBlank
	private String descricao;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataVencimento;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataPagamento;
	
	private BigDecimal valor;
	
	private String observacao;
	
	private TipoLancamento tipoLancamento;
	
	private Long categoriaId;
	
	private Long pessoaId;
	
	public LancamentoResponse(Lancamento entity) {
		this.id = entity.getId();
		this.descricao = entity.getDescricao() != null? entity.getDescricao() : "";
		this.dataVencimento = entity.getDataVencimento() != null ? entity.getDataVencimento() : LocalDate.now();
		this.dataPagamento = entity.getDataPagamento() != null ? entity.getDataPagamento() : LocalDate.now();
		this.valor = entity.getValor() != null? entity.getValor() : null;
		this.observacao = entity.getObservacao() != null ? entity.getObservacao() : "";
		this.tipoLancamento = entity.getTipoLancamento() != null ? entity.getTipoLancamento() : null;
		this.categoriaId = entity.getCategoria().getId();
		this.pessoaId = entity.getPessoa().getId();
	}
}
