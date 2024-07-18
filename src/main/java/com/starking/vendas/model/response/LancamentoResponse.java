package com.starking.vendas.model.response;

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
	
	public LancamentoResponse(Lancamento entity) {
		this.id = entity.getId();
		this.descricao = entity.getDescricao();
	}

}
