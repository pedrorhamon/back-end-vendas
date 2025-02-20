package com.starking.vendas.repositories;

import com.starking.vendas.model.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
}
