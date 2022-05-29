package org.telematix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.telematix.models.User;
import org.telematix.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {
    @Mock
    UserRepository userModelRepository;

    UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        userDetailService = new UserDetailService(userModelRepository);
    }

    @Test
    void get_user_details_without_errors() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        doReturn(Optional.of(user)).when(userModelRepository).findByUsername("test");
        UserDetails userDetails = userDetailService.loadUserByUsername("test");
        assertEquals(userDetails.getUsername(), user.getUsername());
    }

    @Test
    void get_user_details_failed_user_empty() {
        doReturn(Optional.empty()).when(userModelRepository).findByUsername("test");
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername("test"));
    }
}