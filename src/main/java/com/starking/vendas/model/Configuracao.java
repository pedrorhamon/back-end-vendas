package com.starking.vendas.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Configuracao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailUsuario;
    private String servidorSMTP;
    private int portaSMTP;
    private String senhaEmail;
    private boolean autenticacaoSMTP;
    private boolean tlsAtivado;
    private String dominio;
    private String urlApi;
    private String nomeSistema;
}
