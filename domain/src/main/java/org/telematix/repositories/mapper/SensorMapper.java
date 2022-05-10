package org.telematix.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.telematix.models.sensor.Sensor;
import org.telematix.models.sensor.SensorType;

public class SensorMapper implements RowMapper<Sensor> {

    @Override
    public Sensor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Sensor sensor = new Sensor();
        sensor.setId(rs.getInt("id"));
        sensor.setSensorType(SensorType.valueOf(rs.getString("sensor_type")));
        sensor.setTitle(rs.getString("title"));
        sensor.setTopic(rs.getString("topic"));
        sensor.setDeviceId(rs.getInt("device_id"));
        return sensor;
    }
}
