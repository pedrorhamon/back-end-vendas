package com.starking.vendas.services;

import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.response.PermissaoResponse;
import com.starking.vendas.repositories.PermissaoRepository;
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

public class PermissaoServiceTest {

    @Mock
    private PermissaoRepository repository;

    @InjectMocks
    private PermissaoService permissaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarTodos() {
        Pageable pageable = PageRequest.of(0, 10);
        Permissao permissao1 = new Permissao();
        permissao1.setName("Permissão 1");

        Permissao permissao2 = new Permissao();
        permissao2.setName("Permissão 2");

        Page<Permissao> permissaoPage = new PageImpl<>(Arrays.asList(permissao1, permissao2));

        when(repository.findAll(pageable)).thenReturn(permissaoPage);

        Page<PermissaoResponse> result = permissaoService.listarTodos(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Permissão 1", result.getContent().get(0).getName());
        assertEquals("Permissão 2", result.getContent().get(1).getName());
    }

}
