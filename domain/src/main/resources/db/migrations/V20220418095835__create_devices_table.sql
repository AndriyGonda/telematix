CREATE TABLE IF NOT EXISTS devices
(
    id      INT GENERATED ALWAYS AS IDENTITY,
    name    VARCHAR(128),
    user_id INT,
    gps     BOOLEAN,
    PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
