package org.telematix.repositories;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.telematix.models.TopicMessage;
import org.telematix.repositories.mapper.MessageMapper;

@Repository
public class MessageRepository implements ModelRepository<TopicMessage> {
    private static final String SELECT_LATEST_MESSAGE = "SELECT * FROM messages WHERE sensor_id=:sensorId ORDER BY timestamp DESC  LIMIT 1";
    public static final String SELECT_BY_INTERVAL = "SELECT * FROM messages WHERE sensor_id=:sensorId AND timestamp BETWEEN :dateFrom AND :dateTo";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MessageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<TopicMessage> saveItem(TopicMessage item) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("raw", item.getRaw());
        parameters.put("sensor_id", item.getSensorId());
        parameters.put("timestamp", item.getTimestamp());
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("messages")
                .usingGeneratedKeyColumns("id");
        Number messageId = jdbcInsert.executeAndReturnKey(parameters);
        item.setId((int) messageId);
        return Optional.of(item);
    }

    public Optional<TopicMessage> getLatestSensorMessage(int sensorId) {
        try {
            Map<String, Integer> sensorParameter = Collections.singletonMap("sensorId", sensorId);
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_LATEST_MESSAGE, sensorParameter, new MessageMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<TopicMessage> getSensorMessagesByInterval(int sensorId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        try {
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
            Map<String, Object> searchParameters = new HashMap<>();
            searchParameters.put("sensorId", sensorId);
            searchParameters.put("dateFrom", Timestamp.from(dateFrom.toInstant(zoneOffset)));
            searchParameters.put("dateTo", Timestamp.from(dateTo.toInstant(zoneOffset)));
            return jdbcTemplate.query(SELECT_BY_INTERVAL, searchParameters, new MessageMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

