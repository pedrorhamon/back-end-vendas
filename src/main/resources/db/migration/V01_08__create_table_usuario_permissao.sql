CREATE TABLE usuario_permissoes (
    usuario_id BIGINT NOT NULL,
    permissoes_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, permissoes_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (permissoes_id) REFERENCES permissao(id) ON DELETE CASCADE
);