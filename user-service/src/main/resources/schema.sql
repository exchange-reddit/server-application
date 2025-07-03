CREATE TABLE IF NOT EXISTS "users" (
    id INT PRIMARY KEY NOT NULL,
    gender VARCHAR(50) NOT NULL,
    first_name VARCHAR(150) NOT NULL,
    middle_name VARCHAR(150),
    last_name VARCHAR(150) NOT NULL,
    date_of_birth DATE NOT NULL,
    private_email VARCHAR(150) UNIQUE NOT NULL,
    private_email_verified BOOLEAN,
    home_uni VARCHAR(150) NOT NULL,
    home_email VARCHAR(150) UNIQUE NOT NULL,
    home_email_verified BOOLEAN,
    password_hash VARCHAR(200) NOT NULL,
    user_name VARCHAR(150) NOT NULL,
    nationality VARCHAR(150) NOT NULL,
    preferred_language VARCHAR(150) DEFAULT 'ENGLISH',
    profile_picture TEXT,
    program VARCHAR(150) NOT NULL,
    is_active BOOLEAN,
    registration_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    -- Fields for EXCHANGE_USER
    exchange_university VARCHAR(150) NULL,
    exchange_email VARCHAR(150) UNIQUE NULL,
    exchange_email_verified BOOLEAN NULL,
    exchange_start DATE NULL,
    exchange_end DATE NULL,
    -- Fields for PROSPECTIVE_USER
    desired_university VARCHAR(150) NULL
);

CREATE TABLE IF NOT EXISTS "logs" (
    id INT PRIMARY KEY NOT NULL,
    auditType VARCHAR(50) NOT NULL DEFAULT 'DEFAULT',
    auditDate DATE NOT NULL,
    updateUser VARCHAR(100) NOT NULL,
    updateIP VARCHAR(100) NOT NULL,
    updateContent TEXT NOT NULL,
    auditResult BIT NOT NULL
);
