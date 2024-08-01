package com.starking.vendas.model.response;

import java.io.Serializable;

import com.starking.vendas.model.Permissao;

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
public class PermissaoResponse implements Serializable{
	
    private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

	public PermissaoResponse(Permissao permissao) {
		this.id = permissao.getId();
		this.name = permissao.getName();
	}
}
