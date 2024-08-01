package com.starking.vendas.config;

import java.io.IOException;

import org.springframework.boot.json.JsonParser;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.starking.vendas.model.Permissao;

public class PermissaoDeserializer extends JsonDeserializer<Permissao> {

	@Override
	public Permissao deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt)
			throws IOException, JacksonException {
		  JsonNode node = p.getCodec().readTree(p);
	        Long id = node.get("id").asLong();
	        String name = node.get("name").asText();
	        return new Permissao(id, name);
	}
}