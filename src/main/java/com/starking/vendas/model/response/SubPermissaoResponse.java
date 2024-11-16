package com.starking.vendas.model.response;

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

    private String name;

    private Long permissaoPrincipal;

    private String permissaoPrincipalName;
}
