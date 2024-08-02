package com.starking.vendas.model.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.enums.TipoLancamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pedroRhamon
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoRequest {
	
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
	
	public LancamentoRequest(Lancamento entity) {
		this.descricao = entity.getDescricao();
		this.dataVencimento = entity.getDataVencimento();
		this.dataPagamento = entity.getDataPagamento();
		this.valor = entity.getValor();
		this.observacao = entity.getObservacao();
		this.tipoLancamento = entity.getTipoLancamento();
		this.categoriaId = entity.getCategoria().getId();
		this.pessoaId = entity.getPessoa().getId();
	}

}