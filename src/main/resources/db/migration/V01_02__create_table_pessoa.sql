CREATE TABLE if exists pessoa (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 1', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 2', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 3', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 4', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 5', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 6', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 7', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 8', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 9', true, now());
INSERT INTO pessoa (name, ativo, created_at) VALUES ('Pessoa 10', true, now());
