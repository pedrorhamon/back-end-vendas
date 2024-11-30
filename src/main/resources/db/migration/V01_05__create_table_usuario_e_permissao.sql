CREATE TABLE IF NOT EXISTS sub_permissao (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    permissao_id BIGINT NOT NULL,
    FOREIGN KEY (permissao_id) REFERENCES permissao (id)
    );

CREATE TABLE IF NOT EXISTS permissao_sub_permissao
(
    permissao_id BIGINT NOT NULL,
    sub_permissao_id BIGINT NOT NULL,
    PRIMARY KEY (permissao_id, sub_permissao_id),
    FOREIGN KEY (permissao_id) REFERENCES permissao (id) ON DELETE CASCADE,
    FOREIGN KEY (sub_permissao_id) REFERENCES sub_permissao (id) ON DELETE CASCADE
);