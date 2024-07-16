package com.starking.vendas.model.response;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.starking.vendas.model.Pessoa;

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
public class PessoaResponse {

	private Long id;

	@NotNull
	@NotBlank
	@Size(min = 5, max = 150)
	private String name;

	@NotNull
	private Boolean ativo = true;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime createdAt;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime updatedAt;

	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	private String estado;

	public PessoaResponse(Pessoa entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.ativo = entity.getAtivo();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();

		if (entity.getEndereco() != null) {
			this.logradouro = entity.getEndereco().getLogradouro();
			this.numero = entity.getEndereco().getNumero();
			this.complemento = entity.getEndereco().getComplemento();
			this.bairro = entity.getEndereco().getBairro();
			this.cep = entity.getEndereco().getCep();
			this.cidade = entity.getEndereco().getCidade();
			this.estado = entity.getEndereco().getEstado();
		}
	}

}
