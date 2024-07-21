package com.starking.vendas.model.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.Permissao;
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
    
    private Boolean ativo = true;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime createdAt;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime updatedAt;
	
    private List<Permissao> permissoes;
    
    public UsuarioRequest(Usuario entity) {
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.senha = entity.getSenha();
		this.ativo = entity.getAtivo();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
		this.permissoes = entity.getPermissoes();
	}
}