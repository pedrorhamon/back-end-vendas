package com.starking.vendas.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.Lancamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pedroRhamon
 */

@Getter
@Setter
public class LancamentoResponse {
	
	private Long id;
	
	@NotNull
	@NotBlank
	private String descricao;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataVencimento;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dataPagamento;
	
	private BigDecimal valor;
	
	private String observacao;
	
	private String tipo;
	
	private Long categoriaId;
	
	private Long pessoaId;
	
	public LancamentoResponse(Lancamento entity) {
		this.id = entity.getId();
		this.descricao = entity.getDescricao();
		this.dataVencimento = entity.getDataVencimento();
		this.dataPagamento = entity.getDataPagamento();
		this.valor = entity.getValor();
		this.observacao = entity.getObservacao();
		this.tipo = entity.getTipoLancamento().name();
		this.categoriaId = entity.getCategoria().getId();
		this.pessoaId = entity.getPessoa().getId();
	}

}
