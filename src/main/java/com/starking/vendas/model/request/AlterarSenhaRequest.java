package com.starking.vendas.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlterarSenhaRequest {

    @NotBlank
    private String senhaAtual;

    @NotBlank
    private String novaSenha;

    @NotBlank
    private String confirmarNovaSenha;
}
