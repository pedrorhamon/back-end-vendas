package com.starking.vendas.services;

import com.starking.vendas.repositories.PessoaRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeocodingService {

    @Autowired
    private PessoaRepository pessoaRepository;

    private final WebClient webClient;

    public GeocodingService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "SpringApp")
                .build();
    }

    public Mono<GeocodingResponse> buscarCoordenadas(String endereco) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", endereco)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(GeocodingResponse[].class)
                .map(response -> response.length >0 ? response[0] : null);
    }

    public Double calcularDistanciaEntrePessoas(Long id1, Long id2) {
        return pessoaRepository.calcularDistancia(id1, id2);
    }

    @Getter
    @Setter
    public static class GeocodingResponse {
        public String lat;
        public String lon;
    }
}
