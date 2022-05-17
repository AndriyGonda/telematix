package org.telematix.repositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.telematix.models.sensor.Sensor;
import org.telematix.repositories.mapper.SensorMapper;

@Repository
public class SensorRepository implements ModelRepository<Sensor> {
    private static final String SELECT_SENSOR_BY_ID = "SELECT * FROM sensors WHERE id=:id";
    private static final String SENSOR_TOPIC_EXCEPTION_FORMATTER = "Sensor with topic %s already exists";
    private static final String DELETE_QUERY = "DELETE FROM sensors WHERE id=:id";
    private static final String UPDATE_SENSOR_QUERY = "UPDATE sensors SET title=:title, topic=:topic, sensor_type=:sensor_type WHERE id=:item_id";
    private static final String SELECT_SENSOR_BY_TOPIC = "SELECT * FROM sensors WHERE topic=:topic";
    private static final String SELECT_DEVICE_SENSORS = "SELECT * FROM sensors WHERE device_id=:deviceId";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SensorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Sensor> getSensorsByDevice(int deviceId) {
        Map<String, Integer> parameters = Collections.singletonMap("deviceId", deviceId);
        return jdbcTemplate.query(SELECT_DEVICE_SENSORS, parameters, new SensorMapper());
    }

    public Optional<Sensor> getByTopic(String topic) {
        try {
            Map<String, String> parameters = Collections.singletonMap("topic", topic);
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_SENSOR_BY_TOPIC, parameters, new SensorMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Sensor> getById(int itemId) {
        try {
            Map<String, Integer> parameters = Collections.singletonMap("id", itemId);
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_SENSOR_BY_ID, parameters, new SensorMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Sensor> saveItem(Sensor item) {
        try {
            Map<String, Object> sensorParameters = new HashMap<>();
            sensorParameters.put("title", item.getTitle());
            sensorParameters.put("topic", item.getTopic());
            sensorParameters.put("sensor_type", item.getSensorType());
            sensorParameters.put("device_id", item.getDeviceId());
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                    .withTableName("sensors")
                    .usingGeneratedKeyColumns("id");
            Number sensorId = simpleJdbcInsert.executeAndReturnKey(sensorParameters);
            item.setId((int) sensorId);
            return Optional.of(item);
        } catch (DuplicateKeyException e) {
            throw new RepositoryException(String.format(SENSOR_TOPIC_EXCEPTION_FORMATTER, item.getTopic()));
        }
    }

    @Override
    public Optional<Sensor> updateItem(int itemId, Sensor item) {
        try {
            Map<String, Object> sensorParameters = new HashMap<>();
            sensorParameters.put("item_id", itemId);
            sensorParameters.put("title", item.getTitle());
            sensorParameters.put("topic", item.getTopic());
            sensorParameters.put("sensor_type", item.getSensorType());
            jdbcTemplate.update(UPDATE_SENSOR_QUERY, sensorParameters);
            return getById(itemId);
        } catch (DuplicateKeyException e) {
            throw new RepositoryException(String.format(SENSOR_TOPIC_EXCEPTION_FORMATTER, item.getTopic()));
        }
    }

    @Override
    public void deleteItem(int itemId) {
        Map<String, Integer> parameters = Collections.singletonMap("id", itemId);
        jdbcTemplate.update(DELETE_QUERY, parameters);
    }
}
