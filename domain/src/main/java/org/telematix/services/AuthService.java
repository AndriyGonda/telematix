package org.telematix.services;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.telematix.dto.user.ProfileUpdateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.models.User;
import org.telematix.repositories.UserRepository;

@Service
public class AuthService {
    public static final String USER_WITH_CURRENT_PRINCIPAL_NOT_FOUND = "User with current principal not found";
    public static final String USER_NOT_FOUND_IN_DATABASE = "User not found in database";
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByPrincipal() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username);
    }

    public User getAuthUser() {
        Optional<User> optionalUser = getUserByPrincipal();
        if (optionalUser.isEmpty()) throw new ItemNotFoundException(USER_WITH_CURRENT_PRINCIPAL_NOT_FOUND);
        return optionalUser.get();
    }
    public UserResponseDto loadProfile() {
        Optional<User> optionalUser = getUserByPrincipal();
        if (optionalUser.isEmpty()) throw new ItemNotFoundException(USER_WITH_CURRENT_PRINCIPAL_NOT_FOUND);
        return new UserResponseDto(optionalUser.get());
    }

    public UserResponseDto updateProfile(ProfileUpdateDto userUpdateDto) {
        Optional<User> optionalUser = getUserByPrincipal();
        if (optionalUser.isEmpty()) throw new ItemNotFoundException(USER_WITH_CURRENT_PRINCIPAL_NOT_FOUND);
        User user = optionalUser.get();
        userRepository.updateItem(user.getId(), userUpdateDto.toModel());
        Optional<User> databaseUser = userRepository.getById(user.getId());
        if (databaseUser.isEmpty()) throw new ItemNotFoundException(USER_NOT_FOUND_IN_DATABASE);
        return new UserResponseDto(databaseUser.get());
    }
}
