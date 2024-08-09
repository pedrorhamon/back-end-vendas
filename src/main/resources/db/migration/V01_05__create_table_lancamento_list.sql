CREATE TABLE IF NOT EXISTS lancamento (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(10,2) NOT NULL,
    observacao VARCHAR(100),
    tipo VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS lancamento_categoria (
    lancamento_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (lancamento_id, categoria_id),
    FOREIGN KEY (lancamento_id) REFERENCES lancamento(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS lancamento_pessoa (
    lancamento_id BIGINT NOT NULL,
    pessoa_id BIGINT NOT NULL,
    PRIMARY KEY (lancamento_id, pessoa_id),
    FOREIGN KEY (lancamento_id) REFERENCES lancamento(id) ON DELETE CASCADE,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
);

