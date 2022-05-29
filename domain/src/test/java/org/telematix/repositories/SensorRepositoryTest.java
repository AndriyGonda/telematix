package org.telematix.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.telematix.models.Device;
import org.telematix.models.User;
import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
@JdbcTest
class SensorRepositoryTest {
    int deviceId;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    SensorRepository sensorRepository;

    @BeforeEach
    void cleanTables() {
        JdbcTestUtils.deleteFromTables(
                namedParameterJdbcTemplate.getJdbcTemplate(),
                "devices",
                "users",
                "sensors"
        );
    }

    @BeforeEach
    void createElements() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@mail.com");
        user.setPasswordHash("test");
        Optional<User> userOptional = userRepository.saveItem(user);
        if (userOptional.isPresent()) {
            User createdUser = userOptional.get();
            Device device = new Device();
            device.setUserId(createdUser.getId());
            device.setName("test");
            deviceRepository.saveItem(device).ifPresent(createdDevice -> deviceId = createdDevice.getId());
        }
    }

    @Test
    void create_sensor_without_errors() {
        Sensor sensor = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTitle("test");
        sensor.setTopic("test");
        sensor.setDeviceId(deviceId);
        sensorRepository.saveItem(sensor).ifPresent(actualSensor -> assertEquals(sensor, actualSensor));
    }

    @Test
    void create_sensor_failed_duplicated_topic() {
        Sensor sensor = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTitle("test");
        sensor.setTopic("test");
        sensor.setDeviceId(deviceId);
        sensorRepository.saveItem(sensor).ifPresent(actualSensor -> assertThrows(RepositoryException.class, () -> sensorRepository.saveItem(sensor)));
    }

    @Test
    void update_sensor_without_errors() {
        Sensor sensor = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTitle("test");
        sensor.setTopic("test");
        sensor.setDeviceId(deviceId);
        sensorRepository.saveItem(sensor).ifPresent(
                createdSensor -> {
                    createdSensor.setSensorType(SensorType.GPS_JSON);
                    sensorRepository.updateItem(createdSensor.getId(), createdSensor).ifPresent(
                            actualSensor -> assertEquals(createdSensor, actualSensor)
                    );
                }
        );
    }

    @Test
    void update_sensor_failed_duplicated_topic() {
        Sensor sensor = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTitle("test");
        sensor.setTopic("test");
        sensor.setDeviceId(deviceId);
        sensorRepository.saveItem(sensor);
        Sensor anotherSensor = new Sensor();
        anotherSensor.setSensorType(SensorType.STRING);
        anotherSensor.setTitle("test2");
        anotherSensor.setTopic("another");
        anotherSensor.setDeviceId(deviceId);
        sensorRepository.saveItem(anotherSensor).ifPresent(anotherCreatedSensor -> {
            anotherCreatedSensor.setTopic("test");
            assertThrows(
                    RepositoryException.class,
                    () -> sensorRepository.updateItem(
                            anotherCreatedSensor.getId(),
                            anotherCreatedSensor
                    )
            );
        });
    }

    @Test
    void get_sensor_by_id_without_errors() {
        Sensor sensor = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTitle("test");
        sensor.setTopic("test");
        sensor.setDeviceId(deviceId);
        sensorRepository.saveItem(sensor).ifPresent(createdSensor -> {
            sensorRepository.getById(sensor.getId()).ifPresent(actualSensor -> assertEquals(createdSensor, actualSensor));
        });
    }

    @Test
    void get_sensor_by_topic_without_errors() {
        Sensor sensor = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTitle("test");
        sensor.setTopic("test");
        sensor.setDeviceId(deviceId);
        sensorRepository.saveItem(sensor).ifPresent(createdSensor -> {
            sensorRepository.getByTopic(sensor.getTopic()).ifPresent(actualSensor -> assertEquals(createdSensor, actualSensor));
        });
    }

    @Test
    void get_sensor_by_id_failed_empty() {
        assertEquals(Optional.empty(), sensorRepository.getById(1));
    }

    @Test
    void get_sensor_by_topic_failed_empty() {
        assertEquals(Optional.empty(), sensorRepository.getByTopic("test"));
    }

    @Test
    void delete_sensor_without_errors() {
        Sensor sensor = new Sensor();
        sensor.setSensorType(SensorType.STRING);
        sensor.setTitle("test");
        sensor.setTopic("test");
        sensor.setDeviceId(deviceId);
        sensorRepository.saveItem(sensor).ifPresent(createdSensor -> {
            sensorRepository.deleteItem(createdSensor.getId());
            assertEquals(Optional.empty(), sensorRepository.getById(createdSensor.getId()));
        });
    }

    @Test
    void get_sensors_by_device() {
        for (int i = 0; i < 10; i++) {
            Sensor sensor = new Sensor();
            sensor.setSensorType(SensorType.STRING);
            sensor.setTitle("test" + i);
            sensor.setTopic("test" + i);
            sensor.setDeviceId(deviceId);
            sensorRepository.saveItem(sensor);
        }
        assertEquals(10, sensorRepository.getSensorsByDevice(deviceId).size());
    }
}

