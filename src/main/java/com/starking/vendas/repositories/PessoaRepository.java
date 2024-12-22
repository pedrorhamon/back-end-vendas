package com.starking.vendas.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.starking.vendas.model.Pessoa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author pedroRhamon
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	
	Page<Pessoa> findById(Long id, Pageable pageable);
	
	Page<Pessoa> findByName(String name, Pageable pageable);

	List<Pessoa> findByNameIn(List<String> pessoaNomes);

	@Query("SELECT p FROM Pessoa p WHERE ST_DWithin(p.coordenadas, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :raio)")
	List<Pessoa> findByProximidade(@Param("longitude") Double longitude, @Param("latitude") Double latitude, @Param("raio") Double raio);



}
