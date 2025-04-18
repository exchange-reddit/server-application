CREATE TABLE IF NOT EXISTS "users" (
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    private_email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    user_id VARCHAR(50) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    is_admin BIT,
    exchange_uni VARCHAR(80),
    home_uni VARCHAR(80),
    exchange_email VARCHAR(80) UNIQUE,
    home_email VARCHAR(80) UNIQUE,
    nationality VARCHAR(50),
    exchange_start DATE,
    exchange_end DATE,
    preferred_language VARCHAR(50),
    is_active BIT
);