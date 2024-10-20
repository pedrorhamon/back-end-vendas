CREATE TABLE IF NOT EXISTS categoria 
(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  image_file BYTEA,
  created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
);