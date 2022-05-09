package org.telematix.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.telematix.models.TopicMessage;

public class MessageMapper implements RowMapper<TopicMessage> {

    @Override
    public TopicMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        TopicMessage topicMessage = new TopicMessage();
        topicMessage.setId(rs.getInt("id"));
        topicMessage.setRaw(rs.getString("raw"));
        topicMessage.setTimestamp(rs.getTimestamp("timestamp"));
        topicMessage.setSensorId(rs.getInt("sensor_id"));
        return topicMessage;
    }
}
