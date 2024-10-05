package com.starking.vendas.repositories;

import com.starking.vendas.model.Categoria;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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



}
