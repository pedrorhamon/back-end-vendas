package com.starking.vendas.resource;

import com.starking.vendas.event.RecursoCriadoEvent;
import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.request.PessoaRequest;
import com.starking.vendas.model.response.PessoaResponse;
import com.starking.vendas.resource.apis_base.ApiPessoaBaseControle;
import com.starking.vendas.services.GeocodingService;
import com.starking.vendas.services.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class PessoaResource extends ApiPessoaBaseControle{
	
	private final PessoaService pessoaService;

	private final GeocodingService geocodingService;
	
	private final ApplicationEventPublisher publisher;
	
	@GetMapping
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<Page<PessoaResponse>> listar(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String name, 
			@PageableDefault(size = 10) Pageable pageable) {
		Page<PessoaResponse> pessoas; 
		
		if (id != null) {
			pessoas = pessoaService.listarPorId(id, pageable);
		} else if (name != null) {
			pessoas = pessoaService.listarPorName(name, pageable);
		} else {
			pessoas = this.pessoaService.listarTodos(pageable);
		}
		
		return !pessoas.isEmpty() ? ResponseEntity.ok(pessoas) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PessoaResponse> getPessoaById(@PathVariable Long id) {
		PessoaResponse pessoaResponse = pessoaService.findById(id);
		return ResponseEntity.ok(pessoaResponse);
	}
	
	@PostMapping
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
    public ResponseEntity<?> criar(@Valid @RequestBody PessoaRequest pessoaRequest, HttpServletResponse response) {
        try {
            PessoaResponse pessoaNew = this.pessoaService.criar(pessoaRequest);
            
            publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaNew.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(pessoaNew);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a pessoa.");
        }
    }
	
	@PutMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
    public ResponseEntity<?> atualizar(@Valid @PathVariable Long id, @RequestBody PessoaRequest pessoaRequest) {
        try {
        	PessoaResponse pessoaAtualizada = this.pessoaService.atualizar(id, pessoaRequest);
            return ResponseEntity.ok(pessoaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar a categoria.");
        }
    }
	
	@PutMapping("/desativar/{id}")
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
	public ResponseEntity<?> desativar(@PathVariable Long id) {
		 this.pessoaService.desativar(id);
		 return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN_PRIVILEGE')")
    public ResponseEntity<?> deletarPessoa(@PathVariable Long id) {
        pessoaService.deletarPessoa(id);
        return ResponseEntity.noContent().build();
    }

	@GetMapping("/proximidades")
	public ResponseEntity<List<PessoaResponse>> buscarPorProximidade(
			@RequestParam Double latitude,
			@RequestParam Double longitude,
			@RequestParam Double raio) {
		List<Pessoa> proximas = pessoaService.buscarPorProximidade(latitude, longitude, raio);
		return ResponseEntity.ok(proximas.stream().map(PessoaResponse::new).collect(Collectors.toList()));
	}

	@GetMapping("/distancia")
	public ResponseEntity<?> calcularDistancia(@RequestParam Long id1, @RequestParam Long id2) {
		Double distancia = geocodingService.calcularDistanciaEntrePessoas(id1, id2);

		if (distancia == null) {
			return ResponseEntity.badRequest().body("Não foi possível calcular a distância entre as pessoas informadas.");
		}

		return ResponseEntity.ok(String.format("A distância entre as pessoas com ID %d e %d é de %.2f metros.", id1, id2, distancia));
	}

	

}
