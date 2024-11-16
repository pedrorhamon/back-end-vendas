package com.starking.vendas.model.request;

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

    private String name;

    private Long permissaoPrincipalId;
}
