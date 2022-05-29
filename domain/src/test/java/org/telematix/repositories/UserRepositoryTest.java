package org.telematix.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.telematix.models.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
@JdbcTest
public class UserRepositoryTest {
    User testUser;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void cleanTables() {
        JdbcTestUtils.deleteFromTables(namedParameterJdbcTemplate.getJdbcTemplate(), "users");
    }

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("test");
        testUser.setPasswordHash("test");
        testUser.setEmail("test@mail.com");
    }

    @Test
    void create_user_without_errors() {
        userRepository.saveItem(testUser).ifPresent(
                actualUser -> {
                    assertEquals(testUser, actualUser);
                }
        );

    }

    @Test
    void create_user_failed_email_empty() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        assertThrows(RepositoryException.class, () -> userRepository.saveItem(user));
    }

    @Test
    void create_user_failed_duplicated() {
        userRepository.saveItem(testUser).ifPresent(
                savedUser -> {
                    assertThrows(RepositoryException.class, () -> userRepository.saveItem(testUser));
                }
        );
    }

    @Test
    void get_user_by_id_without_errors() {
        userRepository.saveItem(testUser).ifPresent(expectedUser -> {
            int userId = expectedUser.getId();
            userRepository.getById(userId).ifPresent(actualUser -> {
                assertEquals(expectedUser, actualUser);
            });
        });
    }

    @Test
    void get_user_by_id_failed_user_not_found() {
        Optional<User> actualUser = userRepository.getById(1);
        assertEquals(Optional.empty(), actualUser);
    }

    @Test
    void test_delete_without_errors() {
        userRepository.saveItem(testUser).ifPresent(expectedUser -> {
            int userId = expectedUser.getId();
            userRepository.deleteItem(userId);
            assertEquals(Optional.empty(), userRepository.getById(userId));
        });
    }

    @Test
    void get_by_username_without_errors() {
        userRepository.saveItem(testUser).ifPresent(expectedUser -> {
            userRepository.findByUsername("test").ifPresent(actualUser -> {
                assertEquals(expectedUser, actualUser);
            });
        });
    }

    @Test
    void get_by_username_failed_not_found() {
        assertEquals(Optional.empty(), userRepository.findByUsername("test23243242"));
    }

    @Test
    void update_user_without_errors() {
        userRepository.saveItem(testUser).ifPresent(user -> {
            User updateEntity = new User();
            updateEntity.setId(user.getId());
            updateEntity.setUsername(user.getUsername());
            updateEntity.setPasswordHash(user.getPasswordHash());
            updateEntity.setEmail(user.getEmail());
            updateEntity.setFirstName("test");
            updateEntity.setAdministrator(false);
            updateEntity.setLastName("test");
            updateEntity.setAvatarUrl("test");
            userRepository.updateItem(user.getId(), updateEntity).ifPresent(actualUser -> {
                assertEquals(updateEntity, actualUser);
            });
        });
    }

    @Test
    void get_users_without_errors() {
        assertDoesNotThrow(() -> userRepository.getAll());
    }
}
