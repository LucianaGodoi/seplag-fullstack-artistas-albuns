CREATE TABLE IF NOT EXISTS refresh_token (
                                             id           BIGSERIAL PRIMARY KEY,
                                             usuario_id   BIGINT      NOT NULL,
                                             token_hash   VARCHAR(64) NOT NULL UNIQUE, -- SHA-256 hex
    expires_at   TIMESTAMP   NOT NULL,
    revoked_at   TIMESTAMP   NULL,
    created_at   TIMESTAMP   DEFAULT NOW(),
    updated_at   TIMESTAMP   DEFAULT NOW(),
    CONSTRAINT fk_refresh_token_usuario FOREIGN KEY (usuario_id)
    REFERENCES app_user(id)
    );

CREATE INDEX IF NOT EXISTS idx_refresh_token_usuario ON refresh_token(usuario_id);
CREATE INDEX IF NOT EXISTS idx_refresh_token_expires ON refresh_token(expires_at);
