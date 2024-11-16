CREATE TABLE IF NOT EXISTS usuario (
    id bigserial NOT NULL,
    email varchar(255) NULL,
    name varchar(255) NULL,
    senha varchar(255) NULL,
    ativo BOOLEAN NOT NULL,
    created_At timestamp without time zone DEFAULT now(),
    updated_At timestamp without time zone DEFAULT now(),
    CONSTRAINT usuario_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS permissao (
	id bigserial NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT permissao_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS usuario_permissoes (
    usuario_id int8 NOT NULL,
    permissoes_id int8 NOT NULL,
    CONSTRAINT pk_usuario_permissoes PRIMARY KEY (usuario_id, permissoes_id),
    CONSTRAINT fk_usuario_permissoes_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id),
    CONSTRAINT fk_usuario_permissoes_permissao FOREIGN KEY (permissoes_id) REFERENCES public.permissao(id)
);