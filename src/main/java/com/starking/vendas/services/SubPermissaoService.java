package com.starking.vendas.services;

import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.SubPermissao;
import com.starking.vendas.model.request.SubPermissaoRequest;
import com.starking.vendas.model.response.PermissaoResponse;
import com.starking.vendas.model.response.SubPermissaoResponse;
import com.starking.vendas.repositories.PermissaoRepository;
import com.starking.vendas.repositories.SubPermissaoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    public SubPermissaoResponse buscarPorId(Long id) {
        return subPermissaoRepository.findById(id).map(SubPermissaoResponse::new).orElse(null);
    }

    @Transactional
    public SubPermissaoResponse salvar(SubPermissaoRequest subPermissaoRequest) {
        Permissao permissaoPrincipal = permissaoRepository.findById(subPermissaoRequest.getPermissaoPrincipalId())
                .orElseThrow(() -> new EntityNotFoundException("Permissão principal não encontrada com o ID: "
                        + subPermissaoRequest.getPermissaoPrincipalId()));

        SubPermissao subPermissao = new SubPermissao();
        subPermissao.setNome(subPermissaoRequest.getNome());
        subPermissao.setPermissao(permissaoPrincipal);

        SubPermissao subPermissaoSalva = subPermissaoRepository.save(subPermissao);

        return new SubPermissaoResponse(subPermissaoSalva);
    }

    public SubPermissaoResponse atualizarSubPermissao(Long id, SubPermissaoRequest subPermissaoRequest) {

        SubPermissao subPermissaoExistente = subPermissaoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("SubPermissão não encontrada com o ID: " + id));

        Permissao permissaoPrincipal = permissaoRepository.findById(subPermissaoRequest.getPermissaoPrincipalId())
                .orElseThrow(() -> new EntityNotFoundException("Permissão principal não encontrada com o ID: "
                        + subPermissaoRequest.getPermissaoPrincipalId()));

        subPermissaoExistente.setNome(subPermissaoRequest.getNome());
        subPermissaoExistente.setPermissao(permissaoPrincipal);

        SubPermissao subPermissaoAtualizada = subPermissaoRepository.save(subPermissaoExistente);
        return new SubPermissaoResponse(subPermissaoAtualizada);
    }

    public SubPermissaoResponse obterSubPermissaoPorId(Long id) {
        SubPermissao subPermissao = subPermissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubPermissão não encontrada com o ID: " + id));

        return new SubPermissaoResponse(subPermissao);
    }
}
