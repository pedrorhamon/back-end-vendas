package com.starking.vendas.model.response;

import com.starking.vendas.model.Permissao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pedroRhamon
 */

@Getter
@Setter
@NoArgsConstructor
public class PermissaoResponse {

	private Long id;
	private String name;

	public PermissaoResponse(Permissao permissao) {
		this.id = permissao.getId();
		this.name = permissao.getName();
	}
}
