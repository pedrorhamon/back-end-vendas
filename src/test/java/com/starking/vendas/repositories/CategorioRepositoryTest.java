package com.starking.vendas.repositories;

import com.starking.vendas.model.Categoria;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AllArgsConstructor
public class CategorioRepositoryTest {

    private final CategoriaRepository categoriaRepository;

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
        categoria.setName("Categoria 1");
        categoriaRepository.save(categoria);

        Categoria categoria2 = new Categoria();
        categoria2.setName("Categoria 2");
        categoriaRepository.save(categoria2);

        List<String> nomesCategorias = Arrays.asList("Categoria 1", "Categoria 2");
        List<Categoria> categoriasEncontradas = categoriaRepository.findByNameIn(nomesCategorias);

        assertEquals(2, categoriasEncontradas.size());
        assertTrue(categoriasEncontradas.stream().anyMatch(c -> c.getName().equals("Categoria 1")));
        assertTrue(categoriasEncontradas.stream().anyMatch(c -> c.getName().equals("Categoria 2")));
    }
}
