package com.starking.vendas.repositories;

import com.starking.vendas.model.Categoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    public void testSaveAndFindById() {
        Categoria categoria = new Categoria();
        categoria.setName("Categoria 1");
        categoriaRepository.save(categoria);

        Optional<Categoria> categoriaEncontrada = categoriaRepository.findById(categoria.getId());
        assertTrue(categoriaEncontrada.isPresent());
        assertEquals("Categoria 1", categoriaEncontrada.get().getName());
    }

    @Test
    public void testFindByNome() {
        Categoria categoria = new Categoria();
        categoria.setName("Categoria 6");
        categoriaRepository.save(categoria);

        Categoria categoria2 = new Categoria();
        categoria2.setName("Categoria 8");
        categoriaRepository.save(categoria2);

        List<String> nomesCategorias = Arrays.asList("Categoria 6", "Categoria 8");
        List<Categoria> categoriasEncontradas = categoriaRepository.findByNameIn(nomesCategorias);

        assertEquals(2, categoriasEncontradas.size());
        assertTrue(categoriasEncontradas.stream().anyMatch(c -> c.getName().equals("Categoria 6")));
        assertTrue(categoriasEncontradas.stream().anyMatch(c -> c.getName().equals("Categoria 8")));
    }

    @Test
    public void testDeleteById() {
        Categoria categoria = new Categoria();
        categoria.setName("Categoria 9");

        Categoria categoriaSalva = categoriaRepository.save(categoria);

        Optional<Categoria> categoriaEncontrada = categoriaRepository.findById(categoriaSalva.getId());
        assertTrue(categoriaEncontrada.isPresent());

        categoriaRepository.delete(categoriaSalva);

        categoriaEncontrada = categoriaRepository.findById(categoriaSalva.getId());
        assertFalse(categoriaEncontrada.isPresent());
    }
}
