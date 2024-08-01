package com.starking.vendas.model.request;

import java.io.Serializable;

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
	private String name;
    
//    public PermissaoRequest(Permissao entity) {
//    	this.name = entity.getName();
//    }
}