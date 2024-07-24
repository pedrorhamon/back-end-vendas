package com.starking.vendas.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.starking.vendas.exceptions.PessoaInexistenteOuInativaException;
import com.starking.vendas.model.Categoria;
import com.starking.vendas.model.Lancamento;
import com.starking.vendas.model.Pessoa;
import com.starking.vendas.model.request.LancamentoRequest;
import com.starking.vendas.model.response.LancamentoResponse;
import com.starking.vendas.repositories.CategoriaRepository;
import com.starking.vendas.repositories.LancamentoRepository;
import com.starking.vendas.repositories.PessoaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class LancamentoService {
	
	private final LancamentoRepository lancamentoRepository;
	private final CategoriaRepository categoriaRepository;
	private final PessoaRepository pessoaRepository;
	
	@Transactional
	public Page<LancamentoResponse> listarTodos(Pageable pageable) {
		Page<Lancamento> lancamentosPage = lancamentoRepository.findAll(pageable);
		return lancamentosPage.map(LancamentoResponse::new);
	}

	public Page<LancamentoResponse> listarPorId(Long id, Pageable pageable) {
		return lancamentoRepository.findById(id, pageable).map(LancamentoResponse::new);
	}

	public Page<LancamentoResponse> listarPorDescricao(String descricao, Pageable pageable) {
		return lancamentoRepository.findByDescricaoContaining(descricao, pageable).map(LancamentoResponse::new);
	}
	
	public Page<LancamentoResponse> listarPorIdEDescricao(Long id, String descricao, Pageable pageable) {
        return lancamentoRepository.findByIdAndDescricaoContaining(id, descricao, pageable)
            .map(LancamentoResponse::new);
    }
	
	 public Page<LancamentoResponse> listarPorData(LocalDate dataVencimentoDe, LocalDate dataVencimentoAte, Pageable pageable) {
	        Page<Lancamento> lancamentos = lancamentoRepository.
	        		findByDataVencimentoBetween(dataVencimentoDe, dataVencimentoAte, pageable);
	        return lancamentos.map(LancamentoResponse::new);
	    }
	
	@Transactional
	public LancamentoResponse criar(LancamentoRequest lancamentoRequest) {
	    Lancamento lancamento = new Lancamento();
	    Optional<Categoria> categoria = categoriaRepository.findById(lancamentoRequest.getCategoriaId());
	    Optional<Pessoa> pessoa = pessoaRepository.findById(lancamentoRequest.getCategoriaId());

	    if (categoria.isEmpty() || pessoa.isEmpty()) {
	        throw new EntityNotFoundException("Categoria or Pessoa not found");
	    }
	    
	    if (!pessoa.get().getAtivo() || pessoa.get().getId() == null) {
	        throw new PessoaInexistenteOuInativaException("Cannot create Lancamento for inactive Pessoa or null");
	    }
	    
	    if (lancamentoRequest.getDataPagamento() != null && lancamentoRequest.getDataVencimento().isBefore(lancamentoRequest.getDataPagamento())) {
	        throw new IllegalArgumentException("Data de Vencimento cannot be before Data de Pagamento");
	    }

	    lancamento.setDescricao(lancamentoRequest.getDescricao());
	    lancamento.setDataVencimento(lancamentoRequest.getDataVencimento());
	    lancamento.setDataPagamento(lancamentoRequest.getDataPagamento());
	    lancamento.setValor(lancamentoRequest.getValor());
	    lancamento.setObservacao(lancamentoRequest.getObservacao());
	    lancamento.setTipoLancamento(lancamentoRequest.getTipoLancamento());
	    lancamento.setCategoria(categoria.get());
	    lancamento.setPessoa(pessoa.get());

	    Lancamento lancamentoSalva = lancamentoRepository.save(lancamento);

	    return new LancamentoResponse(lancamentoSalva);
	}
	
	@Transactional
	public LancamentoResponse atualizar(Long id, LancamentoRequest lancamentoRequest) {
	    return lancamentoRepository.findById(id).map(lancamentoExistente -> {
	        Optional<Categoria> categoria = categoriaRepository.findById(lancamentoRequest.getCategoriaId());
	        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamentoRequest.getPessoaId());

	        if (categoria.isEmpty() || pessoa.isEmpty()) {
	            throw new EntityNotFoundException("Categoria or Pessoa not found");
	        }
	        
	        if (!pessoa.get().getAtivo()) {
	            throw new IllegalStateException("Cannot create Lancamento for inactive Pessoa");
	        }
	        
	        if (lancamentoRequest.getDataPagamento() != null && lancamentoRequest.getDataVencimento().isBefore(lancamentoRequest.getDataPagamento())) {
	            throw new IllegalArgumentException("Data de Vencimento cannot be before Data de Pagamento");
	        }

	        lancamentoExistente.setDescricao(lancamentoRequest.getDescricao());
	        lancamentoExistente.setDataVencimento(lancamentoRequest.getDataVencimento());
	        lancamentoExistente.setDataPagamento(lancamentoRequest.getDataPagamento());
	        lancamentoExistente.setValor(lancamentoRequest.getValor());
	        lancamentoExistente.setObservacao(lancamentoRequest.getObservacao());
	        lancamentoExistente.setTipoLancamento(lancamentoRequest.getTipoLancamento());
	        lancamentoExistente.setCategoria(categoria.get());
	        lancamentoExistente.setPessoa(pessoa.get());

	        Lancamento lancamentoAtualizado = lancamentoRepository.save(lancamentoExistente);

	        return new LancamentoResponse(lancamentoAtualizado);
	    }).orElseThrow(() -> new EntityNotFoundException("Lancamento not found with ID: " + id));
	}
	
	@Transactional
	public void deletarLancamento(Long id) {
		Lancamento lancamento = lancamentoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado com o ID: " + id));
		lancamentoRepository.delete(lancamento);
	}
	
	public ByteArrayInputStream exportarLancamentosParaExcel(Pageable pageable) {
		Page<LancamentoResponse> lancamentosPage = listarTodos(pageable);
		List<LancamentoResponse> lancamentos = lancamentosPage.getContent();
		
		try(Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Lançamentos");
			
			CellStyle headerCellStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerCellStyle.setFont(headerFont);
			headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			Row headerRow = sheet.createRow(0);
			String[] headers = { "ID", "Descrição",
					"Data de Vencimento", "Data de Pagamento",
					"Valor", "Observação",
					"Tipo", "Categoria ID", "Pessoa ID" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerCellStyle);
			}
			
			int rowIdx = 1;
			for (LancamentoResponse lancamento : lancamentos) {
				Row row = sheet.createRow(rowIdx++);
				Object[] lancamentoData = { lancamento.getId(), 
						lancamento.getDescricao(),
						lancamento.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						lancamento.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						lancamento.getValor().doubleValue(), lancamento.getObservacao(),
						lancamento.getTipoLancamento().name(), 
						lancamento.getCategoriaId(), lancamento.getPessoaId() };
				int cellIdx = 0;
				for (Object field : lancamentoData) {
					row.createCell(cellIdx++).setCellValue(field.toString());
				}
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Falha ao exportar dados para Excel", e);
		}
	}
}
