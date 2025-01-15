CREATE DATABASE mctg;
--GRANT ALL PRIVILEGES ON DATABASE dist TO swenuser;
\c mctg

-- Table for decks
CREATE TABLE decks (
                       id serial PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
                       id serial PRIMARY KEY,
                       username VARCHAR ( 255 ) NOT NULL,
                       password VARCHAR (255 ) NOT NULL,
                       name VARCHAR (255 ),
                       bio VARCHAR (255 ),
                       image VARCHAR (255 ),
                       --token VARCHAR ( 255 ) NOT NULL,
                       last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       deck_id INTEGER,
                       FOREIGN KEY (deck_id) REFERENCES decks(id)
);

CREATE TABLE sessions (
                       id serial PRIMARY KEY,
                       userid NUMERIC NOT NULL,
                       token VARCHAR (255 ) NOT NULL,
                       last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table for packages
CREATE TABLE packages (
                          id VARCHAR(255) PRIMARY KEY,
                          price INT NOT NULL,
                          user_id INTEGER,
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Table for cards
CREATE TABLE cards (
                       id VARCHAR(255) PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       damage DOUBLE PRECISION NOT NULL,
                       package_id VARCHAR(255),
                       deck_id INTEGER,
                       FOREIGN KEY (package_id) REFERENCES packages(id),
                       FOREIGN KEY (deck_id) REFERENCES decks(id)
);
INSERT INTO users (username, password)
VALUES
    --('john_doe', 'password123', 'token_john'),
    --('jane_smith', 'securePass456', 'token_jane'),
    --('michael_lee', 'myPassword789', 'token_michael'),
    --('emily_watson', 'passEmily@321', 'token_emily'),
    --('david_jones', 'david!pass987', 'token_david');
    ('john_doe', 'password123'),
    ('jane_smith', 'securePass456'),
    ('michael_lee', 'myPassword789'),
    ('emily_watson', 'passEmily@321'),
    ('david_jones', 'david!pass987');