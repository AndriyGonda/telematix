CREATE TABLE IF NOT EXISTS users(
  id int GENERATED ALWAYS AS IDENTITY,
  username VARCHAR(128) UNIQUE,
  email VARCHAR(128) NOT NULL UNIQUE,
  password_hash VARCHAR(128),
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  avatar_url varchar(256),
  administrator BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (id)
);