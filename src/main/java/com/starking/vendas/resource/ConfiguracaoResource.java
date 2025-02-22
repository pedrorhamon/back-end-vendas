package com.starking.vendas.resource;

import com.starking.vendas.model.Configuracao;
import com.starking.vendas.services.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/configuracoes")
@CrossOrigin(origins = "*")
public class ConfiguracaoResource {

    @Autowired
    private ConfiguracaoService configuracaoService;

    public Configuracao obterConfiguracoes() {
        return configuracaoService.obterConfiguracoes();
    }
}
