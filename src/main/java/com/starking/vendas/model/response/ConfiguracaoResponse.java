package com.starking.vendas.model.response;

import com.starking.vendas.model.Configuracao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfiguracaoResponse {

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

    public ConfiguracaoResponse(Configuracao entity) {
        this.id = entity.getId();
        this.emailUsuario = entity.getEmailUsuario();
        this.servidorSMTP = entity.getServidorSMTP();
        this.portaSMTP = entity.getPortaSMTP();
        this.senhaEmail = entity.getSenhaEmail();
        this.autenticacaoSMTP = entity.isAutenticacaoSMTP();
        this.tlsAtivado = entity.isTlsAtivado();
        this.dominio = entity.getDominio();
        this.urlApi = entity.getUrlApi();
        this.nomeSistema = entity.getNomeSistema();
    }
}
