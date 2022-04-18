CREATE TABLE IF NOT EXISTS sensors
(
    id          INT GENERATED ALWAYS AS IDENTITY,
    title       VARCHAR(255),
    topic       VARCHAR(255),
    sensor_type VARCHAR(30),
    device_id   int,
    PRIMARY KEY (id),
    CONSTRAINT fk_device FOREIGN KEY (device_id) REFERENCES devices (id) ON DELETE CASCADE
)