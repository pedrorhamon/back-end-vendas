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
        Permissao permissaoPrincipal = validarPermissaoPrincipal(subPermissaoRequest.getPermissaoPrincipalId());

        SubPermissao subPermissao = new SubPermissao();
        atualizarEntidadeSubPermissao(subPermissao, subPermissaoRequest, permissaoPrincipal);

        SubPermissao subPermissaoSalva = subPermissaoRepository.save(subPermissao);

        return new SubPermissaoResponse(subPermissaoSalva);
    }

    @Transactional
    public SubPermissaoResponse atualizarSubPermissao(Long id, SubPermissaoRequest subPermissaoRequest) {
        SubPermissao subPermissaoExistente = subPermissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubPermissão não encontrada com o ID: " + id));
        Permissao permissaoPrincipal = validarPermissaoPrincipal(subPermissaoRequest.getPermissaoPrincipalId());

        atualizarEntidadeSubPermissao(subPermissaoExistente, subPermissaoRequest, permissaoPrincipal);

        SubPermissao subPermissaoAtualizada = subPermissaoRepository.save(subPermissaoExistente);
        return new SubPermissaoResponse(subPermissaoAtualizada);
    }

    private Permissao validarPermissaoPrincipal(Long permissaoPrincipalId) {
        return permissaoRepository.findById(permissaoPrincipalId)
                .orElseThrow(() -> new EntityNotFoundException("Permissão principal não encontrada com o ID: " + permissaoPrincipalId));
    }

    private void atualizarEntidadeSubPermissao(SubPermissao subPermissao, SubPermissaoRequest subPermissaoRequest, Permissao permissaoPrincipal) {
        subPermissao.setNome(subPermissaoRequest.getNome());
        subPermissao.setPermissao(permissaoPrincipal);
    }

    public SubPermissaoResponse obterSubPermissaoPorId(Long id) {
        SubPermissao subPermissao = subPermissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubPermissão não encontrada com o ID: " + id));

        return new SubPermissaoResponse(subPermissao);
    }

    @Transactional
    public void excluirSubPermissao(Long id) {
        SubPermissao subPermissao = subPermissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubPermissão não encontrada com o ID: " + id));
        subPermissaoRepository.delete(subPermissao);
    }
}
