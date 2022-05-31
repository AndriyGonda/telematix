CREATE TABLE IF NOT EXISTS messages
(
    id        int GENERATED ALWAYS AS IDENTITY,
    timestamp TIMESTAMP default now(),
    raw       VARCHAR(255),
    sensor_id int,
    PRIMARY KEY (id),
    CONSTRAINT fk_sensor FOREIGN KEY (sensor_id) REFERENCES sensors (id) ON DELETE CASCADE
);