CREATE TABLE IF NOT EXISTS "communities" (
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(100) UNIQUE NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    logo_image TEXT,
    background_image TEXT,
    description TEXT,
    city VARCHAR(150) NOT NULL,
    country VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    website VARCHAR(200),
    phone_number VARCHAR(50)
);
