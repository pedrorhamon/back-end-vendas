package com.starking.vendas.services;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.starking.vendas.infra.ViaCepService;
import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.embedded.Endereco;
import com.starking.vendas.model.request.PessoaRequest;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.model.response.PessoaResponse;
import com.starking.vendas.repositories.PessoaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class PessoaService {
	
	private final PessoaRepository pessoaRepository;
	
    private final ViaCepService viaCepService;
	
	public Page<PessoaResponse> listarTodos(Pageable pageable) {
		Page<Pessoa> pessoaPage = pessoaRepository.findAll(pageable);
		return pessoaPage.map(PessoaResponse::new);
	}
	
	public Page<PessoaResponse> listarPorId(Long id, Pageable pageable) {
		return pessoaRepository.findById(id, pageable).map(PessoaResponse::new);
	}
	
	public Page<PessoaResponse> listarPorName(String name, Pageable pageable) {
		return pessoaRepository.findByName(name, pageable).map(PessoaResponse::new);
	}
	
	@Transactional
	public PessoaResponse criar(PessoaRequest pessoaRequest) {
		Pessoa pessoa = new Pessoa();
		pessoa.setName(pessoaRequest.getName());
		pessoa.setAtivo(pessoaRequest.getAtivo());
		pessoa.setCreatedAt(LocalDateTime.now());

		Endereco endereco = viaCepService.buscarEnderecoPorCep(pessoaRequest.getCep());

		if (endereco != null) {
            endereco.setNumero(pessoaRequest.getNumero());
            endereco.setComplemento(pessoaRequest.getComplemento());
            endereco.setCep(pessoaRequest.getCep());
            pessoa.setEndereco(endereco);
        }

		Pessoa pessoaSalva = this.pessoaRepository.save(pessoa);

		return new PessoaResponse(pessoaSalva);
	}
	
	@Transactional
    public PessoaResponse atualizar(Long id, PessoaRequest pessoaRequest) {
        return pessoaRepository.findById(id).map(pessoaExistente -> {
            pessoaExistente.setName(pessoaRequest.getName());
            pessoaExistente.setAtivo(pessoaRequest.getAtivo());
            pessoaExistente.setUpdatedAt(LocalDateTime.now());

            // Buscar o endereço atualizado pela API ViaCEP
            Endereco endereco = viaCepService.buscarEnderecoPorCep(pessoaRequest.getCep());
            if (endereco != null) {
                endereco.setNumero(pessoaRequest.getNumero());
                endereco.setComplemento(pessoaRequest.getComplemento());
                pessoaExistente.setEndereco(endereco);
            }

            return new PessoaResponse(pessoaRepository.save(pessoaExistente));
        }).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com o ID: " + id));
    }
	
	@Transactional
    public PessoaResponse desativar(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pessoa not found with id " + id));

        pessoa.setAtivo(false);
        Pessoa pessoaDesativada = pessoaRepository.save(pessoa);

        return new PessoaResponse(pessoaDesativada);
    }
	

    @Transactional
    public void deletarPessoa(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com o ID: " + id));
        pessoaRepository.delete(pessoa);
    }
	
	public PessoaResponse findById(Long id) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria not found"));
		return new PessoaResponse(pessoa);
	}

}
