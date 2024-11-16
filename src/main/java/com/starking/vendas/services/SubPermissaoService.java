package com.starking.vendas.services;

import com.starking.vendas.model.response.SubPermissaoResponse;
import com.starking.vendas.repositories.PermissaoRepository;
import com.starking.vendas.repositories.SubPermissaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubPermissaoService {

    private final SubPermissaoRepository subPermissaoRepository;
    private final PermissaoRepository permissaoRepository;

    public Page<SubPermissaoResponse> listarTodas(Pageable pageable) {
        return subPermissaoRepository.findAll(pageable).map(SubPermissaoResponse::new);
    }
}
