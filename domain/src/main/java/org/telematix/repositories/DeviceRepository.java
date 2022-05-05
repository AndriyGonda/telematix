package org.telematix.repositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.telematix.models.Device;
import org.telematix.repositories.mapper.DeviceMapper;

@Repository
public class DeviceRepository implements ModelRepository<Device> {
    private static final String SELECT_DEVICES = "SELECT * FROM devices";
    private static final String SELECT_DEVICE_BY_ID = "SELECT * from devices WHERE id=:id";
    private static final String UPDATE_DEVICE = "UPDATE devices SET name=:name, user_id=:user_id, gps=:gps WHERE id=:deviceId";
    private static final String DELETE_DEVICE = "DELETE FROM devices WHERE id=:deviceId";
    private static final String SELECT_DEVICES_BY_USER_ID = "SELECT * FROM devices WHERE user_id=:userId";
    public static final String SELECT_DEVICE_FOR_USER = "SELECT * FROM devices WHERE id=:deviceId AND user_id=:userId";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DeviceRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Device> filterDevicesByUserId(int userId) {
        return jdbcTemplate.query(SELECT_DEVICES_BY_USER_ID,
                Collections.singletonMap("userId", userId),
                new DeviceMapper()
        );
    }

    public Optional<Device> getByUserAndId(int userId, int itemId) {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("deviceId", itemId);
        parameters.put("userId", userId);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_DEVICE_FOR_USER, parameters, new DeviceMapper()));
    }
    @Override
    public List<Device> getAll() {
        return jdbcTemplate.query(SELECT_DEVICES, new DeviceMapper());
    }

    @Override
    public Optional<Device> getById(int itemId) {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("id", itemId);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_DEVICE_BY_ID, parameters, new DeviceMapper()));
    }

    @Override
    public Optional<Device> saveItem(Device item) {
        Map<String, Object> deviceParameters = new HashMap<>();
        deviceParameters.put("name", item.getName());
        deviceParameters.put("user_id", item.getUserId());
        deviceParameters.put("gps", item.isGps());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("devices")
                .usingGeneratedKeyColumns("id");
        Number deviceId = simpleJdbcInsert.executeAndReturnKey(deviceParameters);
        item.setId((int) deviceId);
        return Optional.of(item);
    }

    @Override
    public Optional<Device> updateItem(int itemId, Device item) {
        Map<String, Object> deviceParameters = new HashMap<>();
        deviceParameters.put("deviceId", itemId);
        deviceParameters.put("name", item.getName());
        deviceParameters.put("user_id", item.getUserId());
        deviceParameters.put("gps", item.isGps());
        jdbcTemplate.update(UPDATE_DEVICE, deviceParameters);
        return ModelRepository.super.updateItem(itemId, item);
    }

    @Override
    public void deleteItem(int itemId) {
        Map<String, Integer> deviceParameterMap = new HashMap<>();
        deviceParameterMap.put("deviceId", itemId);
        jdbcTemplate.update(DELETE_DEVICE, deviceParameterMap);
    }
}
