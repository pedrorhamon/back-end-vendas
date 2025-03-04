package com.starking.vendas.resource;

import com.starking.vendas.model.Configuracao;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.model.response.ConfiguracaoResponse;
import com.starking.vendas.services.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracoes")
@CrossOrigin(origins = "*")
public class ConfiguracaoResource {

    @Autowired
    private ConfiguracaoService configuracaoService;

    @GetMapping
    public ResponseEntity<Page<ConfiguracaoResponse>> obterConfiguracoes(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ConfiguracaoResponse> configuracao = this.configuracaoService.obterConfiguracoes(pageable);
        return !configuracao.isEmpty() ? ResponseEntity.ok(configuracao) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
    }

    @PostMapping
    public ConfiguracaoResponse adicionarConfiguracao(@RequestBody Configuracao configuracao) {
        return configuracaoService.salvarConfiguracao(configuracao);
    }
}
