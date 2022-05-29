package org.telematix.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.telematix.models.User;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = TestContextConfiguration.class)
@JdbcTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void create_user_without_errors() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        user.setEmail("test@mail.com");
        userRepository.saveItem(user).ifPresent(
                actualUser -> {
                    assertEquals(user, actualUser);
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
}
