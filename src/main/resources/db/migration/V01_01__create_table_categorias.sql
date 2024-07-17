CREATE TABLE  categoria 
(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert categories
INSERT INTO categoria (name) VALUES ('Categoria 1');
INSERT INTO categoria (name) VALUES ('Categoria 2');
INSERT INTO categoria (name) VALUES ('Categoria 3');
INSERT INTO categoria (name) VALUES ('Categoria 4');
INSERT INTO categoria (name) VALUES ('Categoria 5');