package com.starking.vendas.model.request;

import java.io.Serializable;

import com.starking.vendas.model.Permissao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissaoRequest  implements Serializable{
	
    private static final long serialVersionUID = 1L;
    private Long id;
	private String name;
    
//    public PermissaoRequest(Permissao entity) {
//    	this.name = entity.getName();
//    }
	
	 public PermissaoRequest(Permissao permissao) {
		 	this.id = permissao.getId();
	        this.name = permissao.getName();
	    }
}