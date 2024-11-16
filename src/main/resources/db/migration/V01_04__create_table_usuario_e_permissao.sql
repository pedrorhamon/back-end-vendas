CREATE TABLE IF NOT EXISTS permissao_hierarquia
(
    id                     BIGSERIAL PRIMARY KEY,
    permissao_id BIGINT NOT NULL,
    subpermissao_id        BIGINT NOT NULL,
    FOREIGN KEY (permissao_id) REFERENCES permissao (id),
    FOREIGN KEY (subpermissao_id) REFERENCES permissao (id)
);