package org.telematix.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.telematix.models.User;
import org.telematix.repositories.mapper.UserMapper;

@Repository
public class UserRepository implements ModelRepository<User> {
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=:id";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    public static final String INSERT_USER_QUERY = "INSERT INTO users(username, email, administrator, password_hash, first_name, last_name, avatar_url) VALUES(:username, :email, :administrator, :password_hash, :first_name, :last_name, :avatar_url) RETURNING id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS, new UserMapper());
    }

    @Override
    public Optional<User> getById(int itemId) {
        Map<String, Integer> parameters = new HashMap<>();
        parameters.put("id", itemId);
        return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_USER_BY_ID, parameters, new UserMapper()));
    }

    @Override
    public Optional<User> saveItem(User item) {
        Map<String, Object> userParameters = new HashMap<>();
        userParameters.put("username", item.getUsername());
        userParameters.put("email", item.getEmail());
        userParameters.put("administrator", item.isAdministrator());
        userParameters.put("password_hash", item.getPasswordHash());
        userParameters.put("first_name", item.getFirstName());
        userParameters.put("last_name", item.getLastName());
        userParameters.put("avatar_url", item.getAvatarUrl());
        int user_id = jdbcTemplate.update(INSERT_USER_QUERY, userParameters);
        item.setId(user_id);
        return Optional.of(item);
    }

    @Override
    public Optional<User> updateItem(int itemId, User item) {
        return Optional.empty();
    }

    @Override
    public void deleteItem(int itemId) {

    }
}
