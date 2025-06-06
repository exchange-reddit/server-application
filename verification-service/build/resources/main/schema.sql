CREATE TABLE IF NOT EXISTS "registration-token" (
    id INT PRIMARY KEY NOT NULL,
    token VARCHAR(10) NOT NULL,
    expiry_date DATE NOT NULL,
    email VARCHAR(50) NOT NULL,
    verificationType INTEGER NOT NULL
);