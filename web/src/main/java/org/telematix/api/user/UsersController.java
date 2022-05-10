package org.telematix.api.user;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.dto.user.UserUpdateDto;
import org.telematix.services.UserService;

@Tag(name = "Users {ADMIN role required}")
@RestController
@RequestMapping("/api")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/users")
    public List<UserResponseDto> listUsers() {
        return userService.getAllUsers();
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user/{userId}")
    public UserResponseDto getUserById(@PathVariable("userId") int userId) {
        return userService.getUserById(userId);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/user/{userId}")
    public UserResponseDto updateUser(@PathVariable("userId") int userId, @RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUser(userId, userUpdateDto);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable("userId") int userId) {
        userService.deleteUser(userId);
    }
}
