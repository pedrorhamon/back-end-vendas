package com.starking.vendas.services;

import com.starking.vendas.exceptions.PessoaInexistenteOuInativaException;
import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.request.CategoriaPessoa;
import com.starking.vendas.model.request.LancamentoRequest;
import com.starking.vendas.model.response.LancamentoResponse;
import com.starking.vendas.repositories.CategoriaRepository;
import com.starking.vendas.repositories.LancamentoRepository;
import com.starking.vendas.repositories.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class LancamentoServiceTest {

    @Mock
    private LancamentoRepository lancamentoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private LancamentoService lancamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Lancamento lancamento1 = new Lancamento();
        lancamento1.setDescricao("Lançamento 1");
        lancamento1.setCategorias(new ArrayList<>());
        lancamento1.setPessoas(new ArrayList<>());

        Lancamento lancamento2 = new Lancamento();
        lancamento2.setDescricao("Lançamento 2");
        lancamento2.setCategorias(new ArrayList<>());
        lancamento2.setPessoas(new ArrayList<>());

        Page<Lancamento> lancamentoPage = new PageImpl<>(Arrays.asList(lancamento1, lancamento2));
        when(lancamentoRepository.findAll(pageable)).thenReturn(lancamentoPage);

        Page<LancamentoResponse> result = lancamentoService.listarTodos(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Lançamento 1", result.getContent().get(0).getDescricao());
        assertEquals("Lançamento 2", result.getContent().get(1).getDescricao());
    }

    @Test
    public void testCreatedLancamentoSuccess() {
        LancamentoRequest request = new LancamentoRequest();
        request.setDescricao("Novo Lançamento");
        request.setValor(new BigDecimal("1500.00"));
        request.setDataVencimento(LocalDate.now().plusDays(5));

        CategoriaPessoa categoriaPessoa1 = new CategoriaPessoa("Categoria 1", "Categoria 1");
        CategoriaPessoa pessoaNome1 = new CategoriaPessoa("Pessoa 1", "Pessoa 1");

        request.setCategoriaNomes(Arrays.asList(categoriaPessoa1));
        request.setPessoaNomes(Arrays.asList(pessoaNome1));

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setName("Categoria 1");

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setName("Pessoa 1");
        pessoa.setAtivo(true);

        Lancamento lancamento = new Lancamento();
        lancamento.setId(1L);
        lancamento.setDescricao(request.getDescricao());
        lancamento.setValor(request.getValor());
        lancamento.setDataVencimento(request.getDataVencimento());

        when(categoriaRepository.findByNameIn(anyList())).thenReturn(Arrays.asList(categoria));
        when(pessoaRepository.findByNameIn(anyList())).thenReturn(Arrays.asList(pessoa));
        when(lancamentoRepository.save(any(Lancamento.class))).thenReturn(lancamento);

        LancamentoResponse result = lancamentoService.criar(request);

        assertNotNull(result);
        assertEquals("Novo Lançamento", result.getDescricao());
    }

    @Test
    public void testCriarLancamentoCategoriaNaoEncontrada() {
        LancamentoRequest request = new LancamentoRequest();
        request.setDescricao("Novo Lançamento");
        request.setValor(new BigDecimal("1500.00"));
        request.setDataVencimento(LocalDate.now().plusDays(5));

        CategoriaPessoa categoriaPessoa1 = new CategoriaPessoa("Categoria 1", "Categoria 1");
        CategoriaPessoa pessoaNome1 = new CategoriaPessoa("Pessoa 1", "Pessoa 1");

        request.setCategoriaNomes(Arrays.asList(categoriaPessoa1));
        request.setPessoaNomes(Arrays.asList(pessoaNome1));


        when(categoriaRepository.findByNameIn(anyList())).thenReturn(Arrays.asList());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            lancamentoService.criar(request);
        });

        assertEquals("Uma ou mais Categorias não foram encontradas", exception.getMessage());
    }

    @Test
    public void testCriarLancamentoPessoaInativa() {
        LancamentoRequest request = new LancamentoRequest();
        request.setDescricao("Novo Lançamento");
        request.setValor(new BigDecimal("1500.00"));
        request.setDataVencimento(LocalDate.now().plusDays(5));

        CategoriaPessoa categoriaPessoa1 = new CategoriaPessoa("Categoria 1", "Categoria 1");
        CategoriaPessoa pessoaNome1 = new CategoriaPessoa("Pessoa 1", "Pessoa 1");

        request.setCategoriaNomes(Arrays.asList(categoriaPessoa1));
        request.setPessoaNomes(Arrays.asList(pessoaNome1));

        Categoria categoria = new Categoria();
        categoria.setName("Categoria 1");

        Pessoa pessoa = new Pessoa();
        pessoa.setName("Pessoa Inativa");
        pessoa.setAtivo(false);

        when(categoriaRepository.findByNameIn(anyList())).thenReturn(Arrays.asList(categoria));
        when(pessoaRepository.findByNameIn(anyList())).thenReturn(Arrays.asList(pessoa));

        Exception exception = assertThrows(PessoaInexistenteOuInativaException.class, () -> {
            lancamentoService.criar(request);
        });

        assertEquals("Não é possível criar um Lançamento para uma Pessoa inativa ou com ID nulo", exception.getMessage());
    }

    @Test
    public void testAtualizarLancamentoComSucesso() {
        Long id = 1L;
        LancamentoRequest request = new LancamentoRequest();
        request.setDescricao("Lançamento Atualizado");
        request.setValor(new BigDecimal("2000.00"));
        request.setDataVencimento(LocalDate.now().plusDays(5));

        CategoriaPessoa categoriaPessoa1 = new CategoriaPessoa("Categoria 1", "Categoria 1");
        CategoriaPessoa pessoaNome1 = new CategoriaPessoa("Pessoa 1", "Pessoa 1");

        request.setCategoriaNomes(Arrays.asList(categoriaPessoa1));
        request.setPessoaNomes(Arrays.asList(pessoaNome1));

        Lancamento lancamentoExistente = new Lancamento();
        lancamentoExistente.setDescricao("Lançamento Original");

        Categoria categoria = new Categoria();
        categoria.setName("Categoria 1");

        Pessoa pessoa = new Pessoa();
        pessoa.setName("Pessoa 1");
        pessoa.setAtivo(true);

        when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamentoExistente));
        when(categoriaRepository.findByNameIn(anyList())).thenReturn(Arrays.asList(categoria));
        when(pessoaRepository.findByNameIn(anyList())).thenReturn(Arrays.asList(pessoa));
        when(lancamentoRepository.save(any(Lancamento.class))).thenReturn(lancamentoExistente);

        LancamentoResponse result = lancamentoService.atualizar(id, request);

        assertNotNull(result);
        assertEquals("Lançamento Atualizado", result.getDescricao());
    }

    @Test
    public void testAtualizarLancamentoNaoEncontrado() {
        Long id = 1L;
        LancamentoRequest request = new LancamentoRequest();
        request.setDescricao("Lançamento Atualizado");

        when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            lancamentoService.atualizar(id, request);
        });

        assertEquals("Lançamento não encontrado com ID: " + id, exception.getMessage());
    }

    @Test
    public void testDeletarLancamentoComSucesso() {
        Long id = 1L;
        Lancamento lancamento = new Lancamento();
        lancamento.setId(id);
        lancamento.setDescricao("Lançamento para deletar");

        when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));

        lancamentoService.deletarLancamento(id);

        verify(lancamentoRepository, times(1)).delete(lancamento);
    }

    @Test
    public void testDeletarLancamentoNaoEncontrado() {
        Long id = 1L;

        when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            lancamentoService.deletarLancamento(id);
        });

        assertEquals("Lançamento não encontrado com o ID: " + id, exception.getMessage());
    }

    @Test
    public void testListarPorDescricao() {
        Pageable pageable = PageRequest.of(0, 10);
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao("Lançamento com Descrição");

        Page<Lancamento> lancamentoPage = new PageImpl<>(Arrays.asList(lancamento));
        when(lancamentoRepository.findByDescricaoContaining(anyString(), eq(pageable))).thenReturn(lancamentoPage);

        Page<LancamentoResponse> result = lancamentoService.listarPorDescricao("Descrição", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Lançamento com Descrição", result.getContent().get(0).getDescricao());
    }
}
