package com.starking.vendas.services;

import com.starking.vendas.infra.ViaCepService;
import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.response.PessoaResponse;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private ViaCepService viaCepService;

    @InjectMocks
    private PessoaService pessoaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setName("Pessoa 1");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setName("Pessoa 2");

        Page<Pessoa> pessoaPage = new PageImpl<>(Arrays.asList(pessoa1, pessoa2));

        when(pessoaRepository.findAll(pageable)).thenReturn(pessoaPage);

        Page<PessoaResponse> result = pessoaService.listarTodos(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Pessoa 1", result.getContent().get(0).getName());
        assertEquals("Pessoa 2", result.getContent().get(1).getName());
    }
}
