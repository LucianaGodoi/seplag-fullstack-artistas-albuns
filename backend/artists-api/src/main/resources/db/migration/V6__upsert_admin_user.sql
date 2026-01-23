INSERT INTO app_user (username, password, role, ativo)
VALUES (
           'admin',
           '$2a$10$jW9SLA5z92vsbVYlj.dsVeLVnaieHU83VuZVNnEAr4kcboev9veFS',
           'ADMIN',
           true
       )
    ON CONFLICT (username)
DO UPDATE SET
    password = EXCLUDED.password,
           role = EXCLUDED.role,
           ativo = EXCLUDED.ativo;