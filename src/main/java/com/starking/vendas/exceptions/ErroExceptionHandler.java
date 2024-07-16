package com.starking.vendas.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pedroRhamon
 */
@ControllerAdvice
@AllArgsConstructor
public class ErroExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String messageInvalida = messageSource.getMessage("mensagem-invalida", null, LocaleContextHolder.getLocale());
		String mensagemDev = ex.getCause().toString();
		return handleExceptionInternal(ex, new Erro(messageInvalida, mensagemDev), headers, HttpStatus.BAD_REQUEST, request);
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Erro {
		private String mensagemUsuario;
		private String mensagemDev;

//		public Erro(String mensagemUsuario, String mensagemDev) {
//			this.mensagemUsuario = mensagemUsuario;
//			this.mensagemDev = mensagemDev;
//		}

	}

}
