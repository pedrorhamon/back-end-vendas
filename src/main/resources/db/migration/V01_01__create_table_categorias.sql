CREATE TABLE IF NOT EXISTS categoria 
(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert categories
INSERT INTO categoria (name) VALUES ('Categoria 1');
INSERT INTO categoria (name) VALUES ('Categoria 2');
INSERT INTO categoria (name) VALUES ('Categoria 3');
INSERT INTO categoria (name) VALUES ('Categoria 4');
INSERT INTO categoria (name) VALUES ('Categoria 5');