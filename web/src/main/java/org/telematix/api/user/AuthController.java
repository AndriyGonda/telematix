package org.telematix.api.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telematix.dto.user.RegisterDto;
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.services.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody RegisterDto registerUserDto) {
        registerUserDto.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setPassword(registerUserDto.getPassword());
        userCreateDto.setUsername(registerUserDto.getUsername());
        userCreateDto.setEmail(registerUserDto.getEmail());
        return userService.createUser(userCreateDto);
    }
}
