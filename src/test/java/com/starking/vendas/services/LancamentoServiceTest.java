package com.starking.vendas.services;

import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.response.LancamentoResponse;
import com.starking.vendas.repositories.CategoriaRepository;
import com.starking.vendas.repositories.LancamentoRepository;
import com.starking.vendas.repositories.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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

}
