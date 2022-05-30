package org.telematix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.telematix.dto.user.ProfileUpdateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.models.User;
import org.telematix.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    StorageService storageService;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;
    AuthService authService;

    @BeforeEach
    void setUp() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        authService = new AuthService(userRepository, storageService);
    }

    @Test
    void get_user_by_principal_without_errors() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        doReturn(Optional.of(user)).when(userRepository).findByUsername(null);
        authService.getUserByPrincipal().ifPresent(actualUser -> {
            assertEquals(actualUser.getUsername(), user.getUsername());
        });
    }

    @Test
    void get_auth_user_without_errors() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        doReturn(Optional.of(user)).when(userRepository).findByUsername(null);
        User actualUser = authService.getAuthUser();
        assertEquals(user.getUsername(), actualUser.getUsername());
    }

    @Test
    void get_auth_user_failed_user_not_found() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(null);
        assertThrows(ItemNotFoundException.class, () -> authService.getAuthUser());
    }

    @Test
    void get_user_profile_without_errors() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        doReturn(Optional.of(user)).when(userRepository).findByUsername(null);
        UserResponseDto expectedUserResponse = new UserResponseDto(user);
        UserResponseDto actualUserResponse = authService.loadProfile();
        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    void get_user_profile_failed_user_not_found() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(null);
        assertThrows(ItemNotFoundException.class, () -> authService.loadProfile());
    }

    @Test
    void update_user_profile_without_errors() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        user.setId(1);
        doReturn(Optional.of(user)).when(userRepository).findByUsername(null);
        doReturn(Optional.of(user)).when(userRepository).getById(1);
        user.setFirstName("test");
        user.setLastName("test");
        user.setAvatarUrl("/test");
        ProfileUpdateDto expectedProfile = new ProfileUpdateDto();
        expectedProfile.setAvatarUrl(user.getAvatarUrl());
        expectedProfile.setFirstName(user.getFirstName());
        expectedProfile.setLastName(user.getLastName());
        UserResponseDto expectedProfileDto = new UserResponseDto(user);

        UserResponseDto actualProfileDto = authService.updateProfile(expectedProfile);

        assertEquals(expectedProfileDto, actualProfileDto);
    }

    @Test
    void update_user_profile_failed_not_found_in_principal() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        user.setId(1);
        ProfileUpdateDto expectedProfile = new ProfileUpdateDto();
        expectedProfile.setAvatarUrl(user.getAvatarUrl());
        expectedProfile.setFirstName(user.getFirstName());
        expectedProfile.setLastName(user.getLastName());
        doReturn(Optional.empty()).when(userRepository).findByUsername(null);
        assertThrows(ItemNotFoundException.class, () -> authService.updateProfile(expectedProfile));
    }

    @Test
    void update_user_profile_failed_result_not_returned_from_database() {
        User user = new User();
        user.setUsername("test");
        user.setPasswordHash("test");
        user.setId(1);
        ProfileUpdateDto expectedProfile = new ProfileUpdateDto();
        expectedProfile.setAvatarUrl(user.getAvatarUrl());
        expectedProfile.setFirstName(user.getFirstName());
        expectedProfile.setLastName(user.getLastName());
        doReturn(Optional.of(user)).when(userRepository).findByUsername(null);
        doReturn(Optional.empty()).when(userRepository).getById(1);
        assertThrows(ItemNotFoundException.class, () -> authService.updateProfile(expectedProfile));
    }
}