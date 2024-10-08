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
        lancamento.setValor(new BigDecimal("1000.00"));
        lancamento.setDataVencimento(LocalDate.now().plusDays(10));
        lancamento.setTipoLancamento(TipoLancamento.DESPESA);

        lancamentoRepository.save(lancamento);

        Page<Lancamento> result = lancamentoRepository.findByDescricaoContaining("test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("test", result.getContent().get(0).getDescricao());
    }

    @Test
    public void testFindByDataVencimentoBetween() {
        Pageable pageable = PageRequest.of(0, 10);

        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao("Pagamento de Serviço");
        lancamento.setValor(new BigDecimal("1000.00"));
        lancamento.setDataVencimento(LocalDate.now().plusDays(5));
        lancamento.setTipoLancamento(TipoLancamento.DESPESA);

        lancamentoRepository.save(lancamento);

        Page<Lancamento> result = lancamentoRepository.findByDataVencimentoBetween(
                LocalDate.now(), LocalDate.now().plusDays(7), pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Pagamento de Serviço", result.getContent().get(0).getDescricao());
    }

    @Test
    public void testFindByIdAndDescricaoContaining() {
        Pageable pageable = PageRequest.of(0, 10);
        Lancamento lancamento = new Lancamento();
//        lancamento.setId(6L);
        lancamento.setDescricao("Recebimento de Produto");
        lancamento.setValor(new BigDecimal("1000.00"));
        lancamento.setDataVencimento(LocalDate.now().plusDays(10));
        lancamento.setTipoLancamento(TipoLancamento.DESPESA);

        lancamentoRepository.saveAndFlush(lancamento);

        Page<Lancamento> result = lancamentoRepository.findByIdAndDescricaoContaining(lancamento.getId(), "Recebimento de Produto", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Recebimento de Produto", result.getContent().get(0).getDescricao());
    }
}
