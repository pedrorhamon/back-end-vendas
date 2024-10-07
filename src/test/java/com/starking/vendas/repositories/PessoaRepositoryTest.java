package com.starking.vendas.repositories;

import com.starking.vendas.model.Pessoa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class PessoaRepositoryTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testFindById() {
        Pessoa pessoa = new Pessoa();
        pessoa.setName("Pessoa 1");
        pessoa.setAtivo(true);
        pessoaRepository.save(pessoa);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Pessoa> pessoaPage = pessoaRepository.findById(pessoa.getId(), pageable);

        assertNotNull(pessoaPage);
        assertEquals(1, pessoaPage.getTotalElements());
        assertEquals("Pessoa 1", pessoaPage.getContent().get(0).getName());
    }

    @Test
    public void testFindByIdNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> pessoaPage = pessoaRepository.findById(999L, pageable);
        assertEquals(0, pessoaPage.getTotalElements());
    }

    @Test
    public void testFindByNome() {
        Pessoa pessoa = new Pessoa();
        pessoa.setName("Pessoa 1");
        pessoa.setAtivo(true);
        pessoaRepository.save(pessoa);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Pessoa> pessoaPage = pessoaRepository.findByName(pessoa.getName(), pageable);

        assertNotNull(pessoaPage);
        assertEquals(1, pessoaPage.getTotalElements());
        assertEquals("Pessoa 1", pessoaPage.getContent().get(0).getName());
    }
}
