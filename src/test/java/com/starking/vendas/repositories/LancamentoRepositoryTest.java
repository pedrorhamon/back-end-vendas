package com.starking.vendas.repositories;

import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.enums.TipoLancamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lancamento> result = lancamentoRepository.findAll(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testFindById() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lancamento> result = lancamentoRepository.findById(1L, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testFindByDescricaoContaining() {
        Pageable pageable = PageRequest.of(0, 10);
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao("test");

        Page<Lancamento> result = lancamentoRepository.findByDescricaoContaining("test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("test", result.getContent().get(0).getDescricao());
    }
}
