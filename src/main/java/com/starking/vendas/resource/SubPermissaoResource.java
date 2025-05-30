//package com.starking.vendas.resource;
//
//import com.starking.vendas.event.RecursoCriadoEvent;
//import com.starking.vendas.model.request.SubPermissaoRequest;
//import com.starking.vendas.model.response.PermissaoResponse;
//import com.starking.vendas.model.response.SubPermissaoResponse;
//import com.starking.vendas.resource.apis_base.ApiPermissaoBaseControle;
//import com.starking.vendas.resource.apis_base.ApiSubPermissaoBaseControle;
//import com.starking.vendas.services.SubPermissaoService;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.Valid;
//import jakarta.validation.ValidationException;
//import lombok.AllArgsConstructor;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@AllArgsConstructor
//public class SubPermissaoResource extends ApiSubPermissaoBaseControle {
//
//    private final SubPermissaoService subPermissaoService;
//
//    private final ApplicationEventPublisher publisher;
//
//    @GetMapping
//    public ResponseEntity<Page<SubPermissaoResponse>> listar(
//            @RequestParam(required = false) Long id,
//            @RequestParam(required = false) String descricao,
//            @PageableDefault(size = 10) Pageable pageable) {
//
//        Page<SubPermissaoResponse> subPermissoes = subPermissaoService.listarTodas(pageable);
//        return !subPermissoes.isEmpty() ? ResponseEntity.ok(subPermissoes)
//                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
//    }
//
//    @PostMapping
//    public ResponseEntity<?> criar(@Valid @RequestBody SubPermissaoRequest subPermissaoRequest, HttpServletResponse response) {
//        try {
//            SubPermissaoResponse subPermissoesNew = this.subPermissaoService.salvar(subPermissaoRequest);
//
//            publisher.publishEvent(new RecursoCriadoEvent(this, response, subPermissoesNew.getId()));
//            return ResponseEntity.status(HttpStatus.CREATED).body(subPermissoesNew);
//        } catch (ValidationException e) {
//            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao criar a SubPermissão.");
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> atualizarSubPermissao(@Valid @PathVariable Long id, @RequestBody SubPermissaoRequest subPermissaoRequest) {
//        try {
//            SubPermissaoResponse subPermissaoAtualiza = subPermissaoService.atualizarSubPermissao(id, subPermissaoRequest);
//
//            return ResponseEntity.ok(subPermissaoAtualiza);
//        } catch (ValidationException e) {
//            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar a SubPermissão.");
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> excluirSubPermissao(@PathVariable Long id) {
//        subPermissaoService.excluirSubPermissao(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<SubPermissaoResponse> obterSubPermissaoPorId(@PathVariable Long id) {
//        return ResponseEntity.ok(subPermissaoService.buscarPorId(id));
//    }
//
//    @GetMapping("/listar")
//    public ResponseEntity<List<SubPermissaoResponse>> listarTodasSubPermissoes() {
//        List<SubPermissaoResponse> subPermissoes = subPermissaoService.listarTodasSubPermissoes();
//        return ResponseEntity.ok(subPermissoes);
//    }
//
//
//}
