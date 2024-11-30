package com.starking.vendas.model.request;

import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.SubPermissao;
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
public class SubPermissaoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private List<Long> permissaoPrincipalId;

    public SubPermissaoRequest (SubPermissao subPermissao) {
        this.nome = subPermissao.getNome();
        this.permissaoPrincipalId = subPermissao.getPermissoes().stream()
                .map(Permissao::getId)    // Mapeia para os IDs
                .collect(Collectors.toList());
    }
}
