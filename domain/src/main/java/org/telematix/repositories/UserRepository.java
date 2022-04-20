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
        return Optional.empty();
    }

    @Override
    public Optional<User> updateItem(int itemId, User item) {
        return Optional.empty();
    }

    @Override
    public void deleteItem(int itemId) {

    }
}
