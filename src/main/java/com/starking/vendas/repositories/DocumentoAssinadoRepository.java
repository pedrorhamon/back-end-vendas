package com.starking.vendas.repositories;

import com.starking.vendas.model.DocumentoAssinado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoAssinadoRepository extends JpaRepository<DocumentoAssinado, Long> {
    List<DocumentoAssinado> findByUsuarioId(Long usuarioId);
}