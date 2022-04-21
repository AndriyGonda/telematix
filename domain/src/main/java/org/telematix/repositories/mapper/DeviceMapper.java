package org.telematix.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.telematix.models.Device;

public class DeviceMapper implements RowMapper<Device> {
    @Override
    public Device mapRow(ResultSet rs, int rowNum) throws SQLException {
        Device device = new Device();
        device.setId(rs.getInt("id"));
        device.setGps(rs.getBoolean("gps"));
        device.setName(rs.getString("name"));
        device.setUserId(rs.getInt("user_id"));
        return device;
    }
}
