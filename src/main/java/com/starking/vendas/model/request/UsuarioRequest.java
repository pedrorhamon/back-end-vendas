package com.starking.vendas.model.request;

import com.starking.vendas.model.Usuario;

import lombok.Data;

/**
 * @author pedroRhamon
 */
@Data
public class UsuarioRequest {
    private String name;
    private String email;
    private String senha;
    
    public UsuarioRequest(Usuario entity) {
    	this.name = entity.getName();
    	this.email = entity.getEmail();
    	this.senha = entity.getSenha();
    }
}
