package com.starking.vendas.repositories;

import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.embedded.Endereco;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PessoaRepositoryTest {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    public void testFindById() {
        Pessoa pessoa = new Pessoa();
        pessoa.setName("Pessoa 1");
        pessoa.setAtivo(true);
        pessoa.setCreatedAt(LocalDateTime.now());

        Endereco endereco = new Endereco();
        endereco.setBairro("Centro");
        endereco.setLogradouro("Rua Exemplo");
        pessoa.setEndereco(endereco);

        pessoaRepository.save(pessoa);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Pessoa> pessoaPage = pessoaRepository.findById(pessoa.getId(), pageable);

        assertNotNull(pessoaPage);
        assertEquals(1, pessoaPage.getTotalElements());
        assertEquals("Pessoa 1", pessoaPage.getContent().get(0).getName());
        assertEquals("Centro", pessoaPage.getContent().get(0).getEndereco().getBairro());
    }

    @Test
    public void testFindByIdNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> pessoaPage = pessoaRepository.findById(999L, pageable);
        assertEquals(0, pessoaPage.getTotalElements());
    }

    @Test
    public void testFindByName() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setName("Pessoa 1");
        pessoa.setAtivo(true);
        pessoa.setCreatedAt(LocalDateTime.now());

        pessoaRepository.save(pessoa);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> pessoaPage = pessoaRepository.findByName("Pessoa 1", pageable);

        assertNotNull(pessoaPage);
        assertEquals(1, pessoaPage.getTotalElements());
        assertEquals("Pessoa 1", pessoaPage.getContent().get(0).getName());
    }

    @Test
    public void testFindByNameNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> pessoaPage = pessoaRepository.findByName("Nome Inexistente", pageable);
        assertEquals(0, pessoaPage.getTotalElements());
    }

    @Test
    public void testFindByNameIn() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setName("Pessoa 1");
        pessoa1.setAtivo(true);
        pessoa1.setCreatedAt(LocalDateTime.now());

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setName("Pessoa 2");
        pessoa2.setAtivo(true);
        pessoa2.setCreatedAt(LocalDateTime.now());

        pessoaRepository.saveAll(Arrays.asList(pessoa1, pessoa2));

        List<String> nomes = Arrays.asList("Pessoa 1", "Pessoa 2");
        List<Pessoa> pessoas = pessoaRepository.findByNameIn(nomes);

        assertNotNull(pessoas);
        assertEquals(2, pessoas.size());
        assertEquals("Pessoa 1", pessoas.get(0).getName());
        assertEquals("Pessoa 2", pessoas.get(1).getName());
    }

    @Test
    public void testFindByNameInEmptyList() {
        List<String> nomes = Arrays.asList("Pessoa 1", "Pessoa 2");
        List<Pessoa> pessoas = pessoaRepository.findByNameIn(nomes);

        assertFalse(pessoas.isEmpty());
    }
}
