package org.telematix.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
@JdbcTest
class DeviceRepositoryTest {
    int userId;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @BeforeEach
    void cleanTables() {
        JdbcTestUtils.deleteFromTables(
                namedParameterJdbcTemplate.getJdbcTemplate(),
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
        userRepository.saveItem(user).ifPresent(createdUser -> userId = createdUser.getId());
    }

    @Test
    void create_device_without_errors() {
        Device device = new Device();
        device.setUserId(userId);
        device.setName("test");
        device.setGps(false);
        deviceRepository.saveItem(device).ifPresent(actualDevice -> {
            assertEquals(device, actualDevice);
        });
    }

    @Test
    void get_devices_without_errors() {
        for (int i = 0; i < 10; i++) {
            Device device = new Device();
            device.setUserId(userId);
            device.setName("test " + i);
            device.setGps(false);
            deviceRepository.saveItem(device);
        }
        assertEquals(deviceRepository.filterDevicesByUserId(userId).size(), 10);
    }

    @Test
    void get_device_by_id_and_user_without_errors() {
        Device device = new Device();
        device.setUserId(userId);
        device.setName("test");
        device.setGps(false);
        deviceRepository.saveItem(device).ifPresent(expectedDevice -> {
            deviceRepository.getByUserAndId(userId, expectedDevice.getId())
                    .ifPresent(actualDevice -> assertEquals(expectedDevice, actualDevice));
        });
    }

    @Test
    void get_by_id_without_errors() {
        Device device = new Device();
        device.setUserId(userId);
        device.setName("test");
        device.setGps(false);
        deviceRepository.saveItem(device).ifPresent(expectedDevice -> {
            deviceRepository.getById(expectedDevice.getId())
                    .ifPresent(actualDevice -> assertEquals(expectedDevice, actualDevice));
        });
    }

    @Test
    void delete_without_errors() {
        Device device = new Device();
        device.setUserId(userId);
        device.setName("test");
        device.setGps(false);
        deviceRepository.saveItem(device).ifPresent(createdDevice -> {
            int deviceId = createdDevice.getId();
            deviceRepository.deleteItem(deviceId);
            assertEquals(Optional.empty(), deviceRepository.getById(deviceId));
        });
    }

    @Test
    void update_device_without_errors() {
        Device device = new Device();
        device.setUserId(userId);
        device.setName("test");
        device.setGps(false);
        deviceRepository.saveItem(device).ifPresent(createdDevice -> {
            int deviceId = createdDevice.getId();
            createdDevice.setName("test2");
            deviceRepository.updateItem(deviceId, createdDevice)
                    .ifPresent(actualDevice -> assertEquals(createdDevice, actualDevice));
        });
    }

    @Test
    void list_devices_without_errors() {
        for (int i = 0; i < 10; i++) {
            Device device = new Device();
            device.setUserId(userId);
            device.setName("test" + i);
            device.setGps(false);
            deviceRepository.saveItem(device);
        }
        assertEquals(deviceRepository.getAll().size(), 10);
    }
}