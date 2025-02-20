CREATE TABLE IF NOT EXISTS configuracoes (
     id SERIAL PRIMARY KEY,
     email_usuario VARCHAR(255) NOT NULL,
     servidor_smtp VARCHAR(255) NOT NULL,
     porta_smtp INT NOT NULL,
     senha_email VARCHAR(255) NOT NULL,
     autenticacao_smtp BOOLEAN NOT NULL DEFAULT true,
     tls_ativado BOOLEAN NOT NULL DEFAULT true,
     dominio VARCHAR(255) NOT NULL,
     url_api VARCHAR(255) NOT NULL,
     nome_sistema VARCHAR(255) NOT NULL
);
