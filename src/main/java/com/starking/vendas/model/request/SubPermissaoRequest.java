package com.starking.vendas.model.request;

import com.starking.vendas.model.SubPermissao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubPermissaoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private Long permissaoPrincipalId;

    public SubPermissaoRequest (SubPermissao subPermissao) {
        this.nome = subPermissao.getNome();
        this.permissaoPrincipalId = subPermissao.getPermissao().getId();
    }
}
