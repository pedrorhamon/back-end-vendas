package com.starking.vendas.model.request;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import com.starking.vendas.model.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pedroRhamon
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	@Size(min = 5, max = 150)
    private String name;
	
	@NotNull
	@NotBlank
    private String email;
	
	@NotNull
	@NotBlank
    private String senha;
    
    private Boolean ativo = true;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
//    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private String createdAt;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
//    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private String updatedAt;
	
//	 private List<String> permissoes;
	 private List<PermissaoRequest> permissoes;

	    public UsuarioRequest(Usuario entity) {
	        this.name = entity.getName();
	        this.email = entity.getEmail();
	        this.senha = entity.getSenha();
	        this.ativo = entity.getAtivo();
	        this.createdAt = entity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
			this.updatedAt = entity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
//	        this.permissoes = entity.getPermissoes().stream()
//	            .map(Permissao::getName)
//	            .collect(Collectors.toList());
//	        this.permissoes = entity.getPermissoes().stream()
//	                .map(PermissaoRequest::new)
//	                .collect(Collectors.toList());
	        this.permissoes = entity.getPermissaoRequests(); // Use o método auxiliar

	    }

}
