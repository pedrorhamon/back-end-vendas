CREATE TABLE IF NOT EXISTS pessoa (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    -- Campos do objeto Endereco embutido
    logradouro VARCHAR(255),
    numero VARCHAR(50),
    complemento VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(100),
    cep VARCHAR(20)
    );

INSERT INTO pessoa (name, ativo, created_at, logradouro, numero, complemento, bairro, cidade, estado, cep)
VALUES ('Pessoa 1', true, now(), 'Rua A', '123', 'Apto 10', 'Centro', 'Cidade A', 'Estado A', '12345-678');

INSERT INTO pessoa (name, ativo, created_at, logradouro, numero, complemento, bairro, cidade, estado, cep)
VALUES ('Pessoa 2', true, now(), 'Rua B', '456', 'Apto 20', 'Bairro B', 'Cidade B', 'Estado B', '23456-789');

INSERT INTO pessoa (name, ativo, created_at, logradouro, numero, complemento, bairro, cidade, estado, cep)
VALUES ('Pessoa 3', true, now(), 'Rua C', '789', '', 'Bairro C', 'Cidade C', 'Estado C', '34567-890');

