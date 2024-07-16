package com.starking.vendas.event;

import org.springframework.context.ApplicationEvent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

/**
 * @author pedroRhamon
 */
@Getter
public class RecursoCriadoEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private HttpServletResponse response;
	private Long id;
	
	
	public RecursoCriadoEvent(Object source, HttpServletResponse response, Long id) {
		super(source);
		this.response = response;
		this.id = id;
	}
}
