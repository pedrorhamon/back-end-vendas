package com.starking.vendas.config.component;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pedroRhamon
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String expiracao;
    private String chaveAssinatura;
    private String refreshExpiracao;
    
    public JwtProperties() {
        this.chaveAssinatura = System.getenv("JWT_CHAVE_ASSINATURA");
        if (this.chaveAssinatura == null) {
            generateKey();
        }
    }
    
    private void generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        secureRandom.nextBytes(key);
        this.chaveAssinatura = Base64.getEncoder().encodeToString(key);
        System.out.println("Chave secreta base64 gerada: " + this.chaveAssinatura);
    }
}