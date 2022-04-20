package org.telematix.api.user;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.models.User;
import org.telematix.services.UserService;

@RestController
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public List<UserResponseDto> listUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/api/users")
    public UserResponseDto createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @GetMapping("/api/user/{userId}")
    public UserResponseDto getUserById(@PathVariable("userId") int userId) {
        return userService.getUserById(userId);
    }
}
