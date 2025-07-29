CREATE TABLE IF NOT EXISTS "communities" (
    id BIGINT PRIMARY KEY NOT NULL,
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

CREATE TABLE IF NOT EXISTS "sections" (
    id BIGINT PRIMARY KEY NOT NULL,
    public_community_id BIGINT NOT NULL,
    thumbnail_post_one VARCHAR(200),
    thumbnail_post_two VARCHAR(200),
    thumbnail_post_three VARCHAR(200),
    CONSTRAINT fk_community FOREIGN KEY (public_community_id)
        REFERENCES communities(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "dependencies" (
    id BIGINT PRIMARY KEY NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    section_id INT NOT NULL,
    post_id INT NOT NULL
);