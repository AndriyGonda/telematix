package org.telematix.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.telematix.models.User;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setAdministrator(rs.getBoolean("administrator"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setEmail(rs.getString("email"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPasswordHash(rs.getString("password_hash"));
        return user;
    }
}
