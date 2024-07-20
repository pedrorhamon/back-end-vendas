package com.starking.vendas.repositories;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.starking.vendas.model.Lancamento;

/**
 * @author pedroRhamon
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	Page<Lancamento> findAll(Pageable pageable);

	Page<Lancamento> findById(Long id, Pageable pageable);
    Page<Lancamento> findByDescricaoContaining(String descricao, Pageable pageable);
    Page<Lancamento> findByIdAndDescricaoContaining(Long id, String descricao, Pageable pageable);
    
    
    @Query("SELECT l FROM Lancamento l WHERE " +
            "(:dataVencimentoDe IS NULL OR l.dataVencimento >= :dataVencimentoDe) AND " +
            "(:dataVencimentoAte IS NULL OR l.dataVencimento <= :dataVencimentoAte)")
     Page<Lancamento> findByDateRange(@Param("dataVencimentoDe") LocalDate dataVencimentoDe,
                                      @Param("dataVencimentoAte") LocalDate dataVencimentoAte,
                                      Pageable pageable);

}
