package org.telematix.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.telematix.models.User;
import org.telematix.repositories.mapper.UserMapper;

@Repository
public class UserRepository implements ModelRepository<User> {
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id=:id";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=:userId";
    public static final String UPDATE_USER_QUERY = "UPDATE users SET administrator=:administrator, first_name=:first_name, last_name=:last_name, avatar_url=:avatar_url WHERE id=:user_id";
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
        try {
            Map<String, Integer> parameters = new HashMap<>();
            parameters.put("id", itemId);
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_USER_BY_ID, parameters, new UserMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> saveItem(User item) {
        try {
            Map<String, Object> userParameters = new HashMap<>();
            userParameters.put("username", item.getUsername());
            userParameters.put("email", item.getEmail());
            userParameters.put("administrator", item.isAdministrator());
            userParameters.put("password_hash", item.getPasswordHash());
            userParameters.put("first_name", item.getFirstName());
            userParameters.put("last_name", item.getLastName());
            userParameters.put("avatar_url", item.getAvatarUrl());
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                    .withTableName("users")
                    .usingGeneratedKeyColumns("id");
            Number userId = simpleJdbcInsert.executeAndReturnKey(userParameters);
            item.setId((int) userId);
            return Optional.of(item);
        } catch (DuplicateKeyException e) {
            throw new RepositoryException(USER_ALREADY_EXISTS);
        }
    }

    @Override
    public Optional<User> updateItem(int itemId, User item) {
        Map<String, Object> userParameters = new HashMap<>();
        userParameters.put("administrator", item.isAdministrator());
        userParameters.put("first_name", item.getFirstName());
        userParameters.put("last_name", item.getLastName());
        userParameters.put("avatar_url", item.getAvatarUrl());
        userParameters.put("user_id", itemId);
        jdbcTemplate.update(UPDATE_USER_QUERY, userParameters);
        return getById(itemId);
    }

    @Override
    public void deleteItem(int itemId) {
        Map<String, Integer> userParameterMap = new HashMap<>();
        userParameterMap.put("userId", itemId);
        jdbcTemplate.update(DELETE_USER_QUERY, userParameterMap);
    }
}
