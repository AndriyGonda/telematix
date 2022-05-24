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
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.repositories.UserRepository;
import org.telematix.validators.ValidationException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userModelRepository;
    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userModelRepository);
    }

    @Test
    void create_user_without_errors() {
        UserCreateDto createUserDto = new UserCreateDto();
        createUserDto.setEmail("test");
        createUserDto.setUsername("test");
        createUserDto.setPassword("password");
        doReturn(Optional.of(createUserDto.toModel())).when(userModelRepository).saveItem(createUserDto.toModel());
        UserResponseDto expectedUserDto = new UserResponseDto(createUserDto.toModel());
        UserResponseDto actualUserDto = userService.createUser(createUserDto);
        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void create_user_failed_password_short() {
        UserCreateDto createUserDto = new UserCreateDto();
        createUserDto.setEmail("test");
        createUserDto.setUsername("test");
        createUserDto.setPassword("p");
        assertThrows(ValidationException.class, () -> userService.createUser(createUserDto));
    }

    @Test
    void create_user_failed_username_short() {
        UserCreateDto createUserDto = new UserCreateDto();
        createUserDto.setEmail("test");
        createUserDto.setUsername("t");
        createUserDto.setPassword("password");
        assertThrows(ValidationException.class, () -> userService.createUser(createUserDto));
    }

    @Test
    void create_user_failed_email_short() {
        UserCreateDto createUserDto = new UserCreateDto();
        createUserDto.setEmail("t");
        createUserDto.setUsername("test");
        createUserDto.setPassword("password");
        assertThrows(ValidationException.class, () -> userService.createUser(createUserDto));
    }

    @Test
    void create_user_failed_retrieve_empty() {
        UserCreateDto createUserDto = new UserCreateDto();
        createUserDto.setEmail("test");
        createUserDto.setUsername("test");
        createUserDto.setPassword("password");
        doReturn(Optional.empty()).when(userModelRepository).saveItem(createUserDto.toModel());
        assertThrows(ServiceException.class, () -> userService.createUser(createUserDto));
    }
}