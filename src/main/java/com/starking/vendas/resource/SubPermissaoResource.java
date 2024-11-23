package com.starking.vendas.resource;

import com.starking.vendas.model.response.SubPermissaoResponse;
import com.starking.vendas.resource.apis_base.ApiPermissaoBaseControle;
import com.starking.vendas.services.SubPermissaoService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SubPermissaoResource extends ApiPermissaoBaseControle {

    private final SubPermissaoService subPermissaoService;

    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<Page<SubPermissaoResponse>> listar(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String descricao,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<SubPermissaoResponse> permissoes = subPermissaoService.listarTodas(pageable);
        return !permissoes.isEmpty() ? ResponseEntity.ok(permissoes)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
    }
}
