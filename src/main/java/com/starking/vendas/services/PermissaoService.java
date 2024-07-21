package com.starking.vendas.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.request.PermissaoRequest;
import com.starking.vendas.model.response.PermissaoResponse;
import com.starking.vendas.repositories.PermissaoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    // Create a new permission
    @Transactional
    public PermissaoResponse criarPermissao(PermissaoRequest permissaoRequest) {
        Permissao permissao = new Permissao();
        permissao.setName(permissaoRequest.getName());
        Permissao permissaoSalva = permissaoRepository.save(permissao);
        return new PermissaoResponse(permissaoSalva);
    }

    public List<PermissaoResponse> listarPermissoes() {
        List<Permissao> permissoes = permissaoRepository.findAll();
        return permissoes.stream()
                .map(PermissaoResponse::new)
                .toList();
    }

    public PermissaoResponse obterPermissaoPorId(Long id) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com o ID: " + id));
        return new PermissaoResponse(permissao);
    }

    @Transactional
    public PermissaoResponse atualizarPermissao(Long id, PermissaoRequest permissaoRequest) {
        return permissaoRepository.findById(id).map(permissaoExistente -> {
            permissaoExistente.setName(permissaoRequest.getName());
            Permissao permissaoAtualizada = permissaoRepository.save(permissaoExistente);
            return new PermissaoResponse(permissaoAtualizada);
        }).orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com o ID: " + id));
    }

    @Transactional
    public void deletarPermissao(Long id) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com o ID: " + id));
        permissaoRepository.delete(permissao);
    }
}