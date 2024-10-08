package com.starking.vendas.repositories;

import com.starking.vendas.model.Lancamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        assertEquals(2, result.getTotalElements());
    }
}
