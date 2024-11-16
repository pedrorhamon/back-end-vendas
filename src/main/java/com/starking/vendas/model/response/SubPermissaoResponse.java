package com.starking.vendas.model.response;

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
public class SubPermissaoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nome;

    private Long permissaoPrincipal;

    private String permissaoPrincipalName;

    public SubPermissaoResponse(SubPermissao subPermissao) {
        this.id = subPermissao.getId();
        this.nome = subPermissao.getNome();
        this.permissaoPrincipal = subPermissao.getPermissao().getId();
        this.permissaoPrincipalName = subPermissao.getPermissao().getName();
    }
}
