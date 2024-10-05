package com.starking.vendas.repositories;

import com.starking.vendas.model.Permissao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PermissaoRepositoryTest {

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Test
    public void testSaveAndFindById() {
        Permissao permissao = new Permissao();
        permissao.setName("Administrador");
        Permissao permissaoSalva = permissaoRepository.save(permissao);

        Optional<Permissao> permissaoEncontrada = permissaoRepository.findById(permissaoSalva.getId());

        assertTrue(permissaoEncontrada.isPresent());
        assertEquals("Administrador", permissaoEncontrada.get().getName());
    }

    @Test
    public void testFindByName() {
        Permissao permissao = new Permissao();
        permissao.setName("Administrador");
        permissaoRepository.save(permissao);
        Permissao permissaoEncontrada = permissaoRepository.findByName("Administrador");

        assertNotNull(permissaoEncontrada);
        assertEquals("Administrador", permissaoEncontrada.getName());
    }
}
