package com.starking.vendas.model.embedded;

import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * @author pedroRhamon
 */
@Embeddable
@Data
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
}
