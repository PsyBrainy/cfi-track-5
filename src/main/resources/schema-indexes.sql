-- Índice compuesto para acelerar búsquedas por cuenta y ordenamiento por fecha
CREATE INDEX IF NOT EXISTS idx_transactions_account_date 
ON transactions(account_id, created_at DESC);

-- Índice para optimizar filtrado por tipo de transacción
CREATE INDEX IF NOT EXISTS idx_transactions_type 
ON transactions(type);

-- Índice en la clave foránea para acelerar los JOINs con cuentas
CREATE INDEX IF NOT EXISTS idx_accounts_user_id 
ON accounts(user_id);
