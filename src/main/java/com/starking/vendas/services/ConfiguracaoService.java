package com.starking.vendas.services;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.Configuracao;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.model.response.ConfiguracaoResponse;
import com.starking.vendas.repositories.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    public Page<ConfiguracaoResponse> obterConfiguracoes(Pageable pageable) {
        Page<Configuracao> configuracaoPage = configuracaoRepository.findAll(pageable);
        return configuracaoPage.map(ConfiguracaoResponse::new);
    }

    public ConfiguracaoResponse salvarConfiguracao(Configuracao configuracao) {
        Configuracao configuracaoSalva = configuracaoRepository.save(configuracao);

        return new ConfiguracaoResponse(configuracaoSalva);
    }
}
