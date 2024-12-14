package com.starking.vendas.model.response;

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
public class SubPermissaoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nome;

//    private List<PermissaoResponse> permissoes;


    public SubPermissaoResponse(SubPermissao subPermissao) {
        this.id = subPermissao.getId();
        this.nome = subPermissao.getNome();
//        this.permissoes = subPermissao.getPermissoes().stream()
//                .map(PermissaoResponse::new)
//                .collect(Collectors.toList());
    }
}
