package com.starking.vendas.services;

import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.request.CategoriaRequest;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.repositories.CategoriaRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoriaServiceTest {

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

    @Test
    public void testFindById() {
        Long id = 1L;
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setName("Categoria 1");

        when(repository.findById(id)).thenReturn(Optional.of(categoria));

        CategoriaResponse result = service.findById(id);

        assertNotNull(result);
        assertEquals("Categoria 1", result.getName());
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 2L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.findById(id);
        });

        String expectedMessage = "Categoria not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSave() throws IOException {
        // Mock do MultipartFile
        MultipartFile mockImageFile = mock(MultipartFile.class);
        when(mockImageFile.getBytes()).thenReturn("fake-image-data".getBytes());
        when(mockImageFile.isEmpty()).thenReturn(false);

        // Cria a request com o nome da categoria
        String name = "Nova Categoria";

        // Mock do objeto Categoria retornado pelo repositório
        Categoria categoria = new Categoria();
        categoria.setName(name);
        categoria.setCreatedAt(LocalDateTime.now());

        // Simula a persistência da categoria
        when(repository.save(any(Categoria.class))).thenReturn(categoria);

        // Chama o método criar no serviço
        CategoriaResponse response = service.criar(name, mockImageFile);

        // Verifica se o objeto retornado não é nulo e contém o nome correto
        assertNotNull(response);
        assertEquals("Nova Categoria", response.getName());

        // Verifica se o método save foi chamado corretamente
        verify(repository, times(1)).save(any(Categoria.class));
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setName("Categoria 1");

        when(repository.findById(id)).thenReturn(Optional.of(categoria));

        service.deletarCategoria(id);

        verify(repository, times(1)).delete(categoria);
    }

    @Test
    public void testUpdate() throws IOException {
        Long id = 1L;
        CategoriaRequest request = new CategoriaRequest();
        request.setName("Categoria Atualizada");

        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(id);
        categoriaExistente.setName("Categoria Antiga");

        when(repository.findById(id)).thenReturn(Optional.of(categoriaExistente));
        when(repository.save(any(Categoria.class))).thenReturn(categoriaExistente);

        CategoriaResponse response = service.atualizar(id, request);

        assertNotNull(response);
        assertEquals("Categoria Atualizada", response.getName());
    }

    @Test
    public void testDeletarCategoriaNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            service.deletarCategoria(id);
        });

        String expectedMessage = "Categoria não encontrada com o ID: " + id;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
