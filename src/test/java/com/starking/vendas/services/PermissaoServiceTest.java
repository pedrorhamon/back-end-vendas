package com.starking.vendas.services;

import com.starking.vendas.model.Permissao;
import com.starking.vendas.model.request.PermissaoRequest;
import com.starking.vendas.model.response.PermissaoResponse;
import com.starking.vendas.repositories.PermissaoRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void testCriarPermissao() {
        PermissaoRequest request = new PermissaoRequest();
        request.setName("Nova Permissão");

        Permissao permissao = new Permissao();
        permissao.setName(request.getName());

        when(repository.save(any(Permissao.class))).thenReturn(permissao);

        PermissaoResponse response = permissaoService.criarPermissao(request);

        assertNotNull(response);
        assertEquals("Nova Permissão", response.getName());
    }

    @Test
    public void testObterPermissaoPorId() {
        Long id = 1L;
        Permissao permissao = new Permissao();
        permissao.setId(id);
        permissao.setName("Permissão 1");

        when(repository.findById(id)).thenReturn(Optional.of(permissao));

        PermissaoResponse result = permissaoService.obterPermissaoPorId(id);

        assertNotNull(result);
        assertEquals("Permissão 1", result.getName());
    }

    @Test
    public void testObterPermissaoPorIdNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            permissaoService.obterPermissaoPorId(id);
        });

        String expectedMessage = "Permissão não encontrada com o ID: " + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAtualizarPermissao() {
        Long id = 1L;
        PermissaoRequest request = new PermissaoRequest();
        request.setName("Permissão Atualizada");

        Permissao permissaoExistente = new Permissao();
        permissaoExistente.setId(id);
        permissaoExistente.setName("Permissão Antiga");

        when(repository.findById(id)).thenReturn(Optional.of(permissaoExistente));
        when(repository.save(any(Permissao.class))).thenReturn(permissaoExistente);

        PermissaoResponse response = permissaoService.atualizarPermissao(id, request);

        assertNotNull(response);
        assertEquals("Permissão Atualizada", response.getName());
    }

    @Test
    public void testAtualizarPermissaoNotFound() {
        Long id = 1L;
        PermissaoRequest request = new PermissaoRequest();
        request.setName("Permissão Atualizada");

        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            permissaoService.atualizarPermissao(id, request);
        });

        String expectedMessage = "Permissão não encontrada com o ID: " + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeletarPermissao() {
        Long id = 1L;
        Permissao permissao = new Permissao();
        permissao.setId(id);
        permissao.setName("Permissão para deletar");

        when(repository.findById(id)).thenReturn(Optional.of(permissao));

        permissaoService.deletarPermissao(id);

        verify(repository, times(1)).delete(permissao);
    }

    @Test
    public void testDeletarPermissaoNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            permissaoService.deletarPermissao(id);
        });

        String expectedMessage = "Permissão não encontrada com o ID: " + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
