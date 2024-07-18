package com.starking.vendas.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.request.LancamentoRequest;
import com.starking.vendas.model.response.LancamentoResponse;
import com.starking.vendas.repositories.CategoriaRepository;
import com.starking.vendas.repositories.LancamentoRepository;
import com.starking.vendas.repositories.PessoaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class LancamentoService {
	
	private final LancamentoRepository lancamentoRepository;
	private final CategoriaRepository categoriaRepository;
	private final PessoaRepository pessoaRepository;
	
	public List<LancamentoResponse> listarTodos() {
	    return this.lancamentoRepository.findAll().stream()
	        .map(LancamentoResponse::new) 
	        .collect(Collectors.toList());
	}
	
	@Transactional
	public LancamentoResponse criar(LancamentoRequest lancamentoRequest) {
	    Lancamento lancamento = new Lancamento();
	    Optional<Categoria> categoria = categoriaRepository.findById(lancamentoRequest.getCategoriaId());
	    Optional<Pessoa> pessoa = pessoaRepository.findById(lancamentoRequest.getCategoriaId());

	    if (categoria.isEmpty() || pessoa.isEmpty()) {
	        throw new EntityNotFoundException("Categoria or Pessoa not found");
	    }

	    lancamento.setDescricao(lancamentoRequest.getDescricao());
	    lancamento.setDataVencimento(lancamentoRequest.getDataVencimento());
	    lancamento.setDataPagamento(lancamentoRequest.getDataPagamento());
	    lancamento.setValor(lancamentoRequest.getValor());
	    lancamento.setObservacao(lancamentoRequest.getObservacao());
	    lancamento.setTipoLancamento(lancamentoRequest.getTipoLancamento());
	    lancamento.setCategoria(categoria.get());
	    lancamento.setPessoa(pessoa.get());

	    Lancamento lancamentoSalva = lancamentoRepository.save(lancamento);

	    return new LancamentoResponse(lancamentoSalva);
	}

}
