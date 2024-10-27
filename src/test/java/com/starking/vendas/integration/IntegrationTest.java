package com.starking.vendas.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

//    @BeforeEach
//    public void setup() {
//        restTemplate = restTemplate.withBasicAuth("username", "password");
//    }

    @Test
    public void testCategoriaEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/categorias", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Categoria 1");
    }

    @Test
    public void testPessoaEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/pessoas", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Pessoa 1");
    }

    @Test
    public void testLancamentoEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/lancamentos", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Lancamento 1"); // Ajuste para o conteúdo esperado
    }

    @Test
    public void testPermissaoEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/permissoes", String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("ADMIN"); // Ajuste para o conteúdo esperado
    }

}

