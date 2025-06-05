CREATE TABLE IF NOT EXISTS "registration_token" (
    id INT PRIMARY KEY NOT NULL,
    code VARCHAR(5) NOT NULL,
    expiry_date DATE NOT NULL,
    email VARCHAR(50) NOT NULL,
    verificationType INTEGER NOT NULL
);