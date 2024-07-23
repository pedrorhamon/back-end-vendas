package com.starking.vendas.infra;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.starking.vendas.model.embedded.Endereco;

/**
 * @author pedroRhamon
 */
@Service
public class ViaCepService {

	private static final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";

	public Endereco buscarEnderecoPorCep(String cep) {
		RestTemplate restTemplate = new RestTemplate();
		Endereco endereco = restTemplate.getForObject(String.format(VIACEP_URL, cep), Endereco.class);
		return endereco;
	}
}
