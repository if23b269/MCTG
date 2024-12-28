CREATE DATABASE mctg;
--GRANT ALL PRIVILEGES ON DATABASE dist TO swenuser;
\c mctg

CREATE TABLE users (
                       id serial PRIMARY KEY,
                       username VARCHAR ( 255 ) NOT NULL,
                       password VARCHAR (255 ) NOT NULL,
                       --token VARCHAR ( 255 ) NOT NULL,
                       last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sessions (
                       id serial PRIMARY KEY,
                       userid NUMERIC NOT NULL,
                       token VARCHAR (255 ) NOT NULL,
                       last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
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