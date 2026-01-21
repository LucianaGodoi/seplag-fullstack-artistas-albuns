CREATE TABLE artist (
                        id BIGSERIAL PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL,
                        tipo VARCHAR(50),
                        created_at TIMESTAMP DEFAULT NOW(),
                        updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE album (
                       id BIGSERIAL PRIMARY KEY,
                       artista_id BIGINT NOT NULL,
                       nome VARCHAR(255) NOT NULL,
                       ano_lancamento INTEGER,
                       created_at TIMESTAMP DEFAULT NOW(),
                       updated_at TIMESTAMP DEFAULT NOW(),
                       CONSTRAINT fk_album_artist FOREIGN KEY (artista_id)
                           REFERENCES artist(id)
);

CREATE TABLE album_image (
                             id BIGSERIAL PRIMARY KEY,
                             album_id BIGINT NOT NULL,
                             object_key VARCHAR(500) NOT NULL,
                             original_filename VARCHAR(255),
                             content_type VARCHAR(100),
                             created_at TIMESTAMP DEFAULT NOW(),
                             CONSTRAINT fk_album_image_album FOREIGN KEY (album_id)
                                 REFERENCES album(id)
);

CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(100) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(50) NOT NULL,
                          ativo BOOLEAN DEFAULT true,
                          created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE regional (
                          id BIGINT PRIMARY KEY,
                          nome VARCHAR(200) NOT NULL,
                          ativo BOOLEAN DEFAULT true,
                          created_at TIMESTAMP DEFAULT NOW()
);
