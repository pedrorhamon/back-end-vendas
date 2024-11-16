CREATE TABLE IF NOT EXISTS sub_permissao (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    permissao_id BIGINT NOT NULL,
    FOREIGN KEY (permissao_id) REFERENCES permissao (id)
    );