package com.starking.vendas.model.request;

import com.starking.vendas.model.Permissao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissaoRequest {
	
    private String name;
    
    public PermissaoRequest(Permissao entity) {
    	this.name = entity.getName();
    }
}