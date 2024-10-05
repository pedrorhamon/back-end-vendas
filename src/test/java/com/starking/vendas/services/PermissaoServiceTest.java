package com.starking.vendas.services;

import com.starking.vendas.repositories.PermissaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PermissaoServiceTest {

    @Mock
    private PermissaoRepository repository;

    @InjectMocks
    private PermissaoService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
