package org.telematix.api.user;

import java.util.Collections;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telematix.dto.user.LoginDto;
import org.telematix.dto.user.RegisterDto;
import org.telematix.dto.user.TokenResponseDto;
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.security.JwtUtil;
import org.telematix.services.ServiceException;
import org.telematix.services.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {
    private static final String INVALID_LOGIN_CREDENTIALS = "Invalid Login Credentials";
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
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

    @PostMapping("/login")
    public TokenResponseDto loginUser(@RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            authManager.authenticate(authenticationToken);
            String token = jwtUtil.generateToken(loginDto.getUsername());
            TokenResponseDto tokenResponseDto = new TokenResponseDto();
            tokenResponseDto.setToken(token);
            return tokenResponseDto;
        } catch (AuthenticationException authExc){
            throw new ServiceException(INVALID_LOGIN_CREDENTIALS);
        }
    }
}
