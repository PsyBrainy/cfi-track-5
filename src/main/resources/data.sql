-- Inserción de Usuario ADMIN inicial para PostgreSQL
-- Password en texto plano: Password123
-- Hash BCrypt generado: $2a$10$T.ZjDLGpH0qT8Buy0oDTseGVHr6CnCUCYvunC6ZREY3tJFWjmnLKG
INSERT INTO usuarios (nombre, apellido, dni, email, password, rol, is_deleted, created_at)
VALUES (
    'Admin',
    'Sistema',
    '00000000',
    'admin@alkywallet.com',
    '$2a$10$T.ZjDLGpH0qT8Buy0oDTseGVHr6CnCUCYvunC6ZREY3tJFWjmnLKG',
    'ADMIN',
    false,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;