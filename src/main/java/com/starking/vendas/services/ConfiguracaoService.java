package com.starking.vendas.services;

import com.starking.vendas.model.Configuracao;
import com.starking.vendas.repositories.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    public Configuracao obterConfiguracoes() {
        List<Configuracao> lista = configuracaoRepository.findAll();
        return lista.isEmpty() ? null : lista.get(0);
    }

    public Configuracao salvarConfiguracao(Configuracao configuracao) {
        return configuracaoRepository.save(configuracao);
    }
}
