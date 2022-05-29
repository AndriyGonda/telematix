package org.telematix.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
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
import org.telematix.models.TopicMessage;
import org.telematix.models.User;
import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
@JdbcTest
class MessageRepositoryTest {
    int sensorId;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void cleanTables() {
        JdbcTestUtils.deleteFromTables(
                namedParameterJdbcTemplate.getJdbcTemplate(),
                "messages",
                "sensors",
                "devices",
                "users"
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
          Optional<Device> deviceOptional = deviceRepository.saveItem(device);
          if (deviceOptional.isPresent()) {
              Device createdDevice = deviceOptional.get();
              Sensor sensor = new Sensor();
              sensor.setDeviceId(createdDevice.getId());
              sensor.setSensorType(SensorType.STRING);
              sensor.setTopic("test");
              sensor.setTitle("test");
              sensorRepository.saveItem(sensor).ifPresent(createdSensor -> sensorId = createdSensor.getId());
          }
        }
    }

    @Test
    void create_message_without_errors() {
        TopicMessage topicMessage = new TopicMessage();
        topicMessage.setSensorId(sensorId);
        topicMessage.setRaw("test");
        topicMessage.setTimestamp(Timestamp.from(Instant.now()));
        messageRepository.saveItem(topicMessage).ifPresent(actualMessage -> {
            assertEquals(topicMessage, actualMessage);
        });
    }

    @Test
    void get_latest_message_without_errors() {
        TopicMessage topicMessage = new TopicMessage();
        topicMessage.setSensorId(sensorId);
        topicMessage.setRaw("test");
        messageRepository.saveItem(topicMessage).ifPresent(expectedMessage -> {
            messageRepository.getLatestSensorMessage(sensorId).ifPresent(actualMessage -> {
                assertEquals(expectedMessage, actualMessage);
            });
        });
    }
}