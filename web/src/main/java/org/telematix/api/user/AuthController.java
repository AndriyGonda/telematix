package org.telematix.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.telematix.dto.user.LoginDto;
import org.telematix.dto.user.ProfileUpdateDto;
import org.telematix.dto.user.RegisterDto;
import org.telematix.dto.user.TokenResponseDto;
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.security.JwtUtil;
import org.telematix.services.AuthService;
import org.telematix.services.ServiceException;
import org.telematix.services.StorageService;
import org.telematix.services.UserService;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api")
public class AuthController {
    private static final String INVALID_LOGIN_CREDENTIALS = "Invalid Login Credentials";
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(PasswordEncoder passwordEncoder, UserService userService, AuthService authService, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authService = authService;
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
        } catch (AuthenticationException authExc) {
            throw new ServiceException(INVALID_LOGIN_CREDENTIALS);
        }
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/profile")
    public UserResponseDto getProfile() {
        return authService.loadProfile();
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/profile")
    public UserResponseDto updateProfile(@RequestBody ProfileUpdateDto userUpdateDto) {
        return authService.updateProfile(userUpdateDto);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "/profile/avatar",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public UserResponseDto uploadAvatar(@RequestParam("file") MultipartFile file) {
        return authService.updateAvatar(file);
    }
}
