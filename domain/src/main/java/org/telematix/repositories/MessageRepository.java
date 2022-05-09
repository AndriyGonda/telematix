package org.telematix.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.telematix.models.Message;

@Repository
public class MessageRepository implements ModelRepository<Message> {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MessageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Message> saveItem(Message item) {
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

}

