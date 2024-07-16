package com.starking.vendas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.embedded.Endereco;
import com.starking.vendas.model.request.PessoaRequest;
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
	
	public List<PessoaResponse> listarTodos() {
	    return this.pessoaRepository.findAll().stream()
	        .map(PessoaResponse::new) 
	        .collect(Collectors.toList());
	}
	
	@Transactional
	public PessoaResponse criar(PessoaRequest pessoaRequest) {
		Pessoa pessoa = new Pessoa();
		pessoa.setName(pessoaRequest.getName());
		pessoa.setAtivo(pessoaRequest.getAtivo());
		pessoa.setCreatedAt(LocalDateTime.now());

		Endereco endereco = new Endereco();
        endereco.setLogradouro(pessoaRequest.getLogradouro());
        endereco.setNumero(pessoaRequest.getNumero());
        endereco.setComplemento(pessoaRequest.getComplemento());
        endereco.setBairro(pessoaRequest.getBairro());
        endereco.setCep(pessoaRequest.getCep());
        endereco.setCidade(pessoaRequest.getCidade());
        endereco.setEstado(pessoaRequest.getEstado());
        pessoa.setEndereco(endereco);

        Pessoa pessoaSalva = this.pessoaRepository.save(pessoa);

        return new PessoaResponse(pessoaSalva);
	}
	
	 @Transactional
	    public PessoaResponse atualizar(Long id, PessoaRequest pessoaRequest) {
	        return pessoaRepository.findById(id).map(pessoaExistente -> {
	            pessoaExistente.setName(pessoaRequest.getName());
	            pessoaExistente.setAtivo(pessoaRequest.getAtivo());
	            pessoaExistente.setUpdatedAt(LocalDateTime.now());

	            Endereco endereco = pessoaExistente.getEndereco();
	            if (endereco == null) {
	                endereco = new Endereco();
	                pessoaExistente.setEndereco(endereco);
	            }
	            endereco.setLogradouro(pessoaRequest.getLogradouro());
	            endereco.setNumero(pessoaRequest.getNumero());
	            endereco.setComplemento(pessoaRequest.getComplemento());
	            endereco.setBairro(pessoaRequest.getBairro());
	            endereco.setCep(pessoaRequest.getCep());
	            endereco.setCidade(pessoaRequest.getCidade());
	            endereco.setEstado(pessoaRequest.getEstado());

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

}
