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

import java.util.List;

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
        // Valida todas as permissões principais (se existirem no banco)
        List<Permissao> permissoesPrincipais = validarPermissoesPrincipais(subPermissaoRequest.getPermissaoPrincipalId());

        // Criação da sub-permissão
        SubPermissao subPermissao = new SubPermissao();
        atualizarEntidadeSubPermissao(subPermissao, subPermissaoRequest, permissoesPrincipais);

        // Salva a sub-permissão
        SubPermissao subPermissaoSalva = subPermissaoRepository.save(subPermissao);

        return new SubPermissaoResponse(subPermissaoSalva);
    }

    @Transactional
    public SubPermissaoResponse atualizarSubPermissao(Long id, SubPermissaoRequest subPermissaoRequest) {
        // Encontra a sub-permissão existente
        SubPermissao subPermissaoExistente = subPermissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubPermissão não encontrada com o ID: " + id));

        // Valida as permissões principais
        List<Permissao> permissoesPrincipais = validarPermissoesPrincipais(subPermissaoRequest.getPermissaoPrincipalId());

        // Atualiza a sub-permissão com os novos dados
        atualizarEntidadeSubPermissao(subPermissaoExistente, subPermissaoRequest, permissoesPrincipais);

        // Salva a sub-permissão atualizada
        SubPermissao subPermissaoAtualizada = subPermissaoRepository.save(subPermissaoExistente);

        return new SubPermissaoResponse(subPermissaoAtualizada);
    }

    private List<Permissao> validarPermissoesPrincipais(List<Long> permissaoPrincipalIds) {
        List<Permissao> permissoes = permissaoRepository.findAllById(permissaoPrincipalIds);
        if (permissoes.size() != permissaoPrincipalIds.size()) {
            throw new EntityNotFoundException("Uma ou mais permissões principais não encontradas.");
        }
        return permissoes;
    }

    // Atualiza a entidade SubPermissao com novas permissões
    private void atualizarEntidadeSubPermissao(SubPermissao subPermissao, SubPermissaoRequest subPermissaoRequest, List<Permissao> permissoesPrincipais) {
        subPermissao.setNome(subPermissaoRequest.getNome());
        subPermissao.setPermissoes(permissoesPrincipais);  // Associa múltiplas permissões
    }

    // Obter sub-permissão por ID
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
