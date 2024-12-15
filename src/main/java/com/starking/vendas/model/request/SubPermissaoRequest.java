package com.starking.vendas.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.SubPermissao;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubPermissaoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    public SubPermissaoRequest (SubPermissao subPermissao) {
        this.nome = subPermissao.getNome();
    }
}
