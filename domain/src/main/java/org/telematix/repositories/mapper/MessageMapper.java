package org.telematix.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.telematix.models.Message;

public class MessageMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setRaw(rs.getString("raw"));
        message.setTimestamp(rs.getTimestamp("timestamp"));
        message.setSensorId(rs.getInt("sensor_id"));
        return message;
    }
}
