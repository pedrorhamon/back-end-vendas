package com.starking.vendas.model.embedded;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * @author pedroRhamon
 */
@Embeddable
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Endereco {
	private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    
    @JsonProperty("localidade")
    private String cidade;
    
    @JsonProperty("uf")
    private String estado;
}
