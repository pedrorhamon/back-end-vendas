package com.starking.vendas.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.repositories.CategoriaRepository;
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

public class CategorioServiceTest {

    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Categoria categoria = new Categoria();
        categoria.setName("Categoria 1");

        Page<Categoria> categoriaPage = new PageImpl<>(Arrays.asList(categoria));

        when(repository.findAll(pageable)).thenReturn(categoriaPage);

        Page<CategoriaResponse> result = service.lista(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Categoria 1", result.getContent().get(0).getName());
    }
}
