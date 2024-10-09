package com.starking.vendas.resource;

import com.starking.vendas.model.Categoria;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.starking.vendas.event.RecursoCriadoEvent;
import com.starking.vendas.model.request.CategoriaRequest;
import com.starking.vendas.model.response.CategoriaResponse;
import com.starking.vendas.resource.apis_base.ApiCategoriaBaseControle;
import com.starking.vendas.services.CategoriaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pedroRhamon
 */
@RestController
@AllArgsConstructor
public class CategoriaResource extends ApiCategoriaBaseControle{
	
	private final CategoriaService categoriaService;
	
	private final ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<Page<CategoriaResponse>> listar(
			@PageableDefault(size = 10) Pageable pageable) {
		Page<CategoriaResponse> categorias = this.categoriaService.lista(pageable);
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoriaResponse> getCategoriaById(@PathVariable Long id) {
		CategoriaResponse categoriaResponse = categoriaService.findById(id);
		return ResponseEntity.ok(categoriaResponse);
	}
	
	
//	@PostMapping
//    public ResponseEntity<?> criar(@Valid @RequestBody CategoriaRequest categoriaRequest, HttpServletResponse response) {
//        try {
//            CategoriaResponse categoriaNew = this.categoriaService.criar(categoriaRequest);
//
//            publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaNew.getId()));
//            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaNew);
//        } catch (ValidationException e) {
//            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a categoria.");
//        }
//    }
//
//
//	@PutMapping("/{id}")
//    public ResponseEntity<?> atualizar(@Valid @PathVariable Long id, @RequestBody CategoriaRequest categoriaRequest) {
//        try {
//            CategoriaResponse categoriaAtualizada = this.categoriaService.atualizar(id, categoriaRequest);
//            return ResponseEntity.ok(categoriaAtualizada);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (ValidationException e) {
//            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar a categoria.");
//        }
//    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestPart("categoria") @Valid CategoriaRequest categoriaRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            HttpServletResponse response) {
        try {
            // Passa a imagem para o serviço
            categoriaRequest.setImageFile(imageFile);

            CategoriaResponse categoriaNew = this.categoriaService.criar(categoriaRequest);

            publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaNew.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaNew);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a categoria.");
        }
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> obterImagem(@PathVariable Long id) {
        byte[] imagem = categoriaService.obterImagemPorId(id);

        if (imagem == null || imagem.length == 0) {
            return ResponseEntity.notFound().build();
        }

        // Retorna a imagem com o tipo correto
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Ajuste o tipo da imagem se necessário
                .body(imagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestPart("categoria") @Valid CategoriaRequest categoriaRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            // Passa a imagem para o serviço
            categoriaRequest.setImageFile(imageFile);

            CategoriaResponse categoriaAtualizada = this.categoriaService.atualizar(id, categoriaRequest);
            return ResponseEntity.ok(categoriaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar a categoria.");
        }
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarPessoa(@PathVariable Long id) {
		this.categoriaService.deletarCategoria(id);
		return ResponseEntity.noContent().build();
	}

}
