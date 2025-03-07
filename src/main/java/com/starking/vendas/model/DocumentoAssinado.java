package com.starking.vendas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documentos_assinados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoAssinado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "conteudo", columnDefinition = "LONGBLOB")
    private byte[] conteudo;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
