package org.telematix.services;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.telematix.dto.user.ProfileUpdateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.models.User;
import org.telematix.repositories.UserRepository;

@Service
public class AuthService {
    public static final String USER_WITH_CURRENT_PRINCIPAL_NOT_FOUND = "User with current principal not found";
    public static final String USER_NOT_FOUND_IN_DATABASE = "User not found in database";
    private final UserRepository userRepository;
    private final StorageService storageService;

    public AuthService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
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
        userUpdateDto.setAvatarUrl(user.getAvatarUrl());
        userRepository.updateItem(user.getId(), userUpdateDto.toModel());
        Optional<User> databaseUser = userRepository.getById(user.getId());
        if (databaseUser.isEmpty()) throw new ItemNotFoundException(USER_NOT_FOUND_IN_DATABASE);
        return new UserResponseDto(databaseUser.get());
    }

    public UserResponseDto updateAvatar(MultipartFile multipartFile) {
        Optional<User> optionalUser = getUserByPrincipal();
        if (optionalUser.isEmpty()) throw new ItemNotFoundException(USER_WITH_CURRENT_PRINCIPAL_NOT_FOUND);
        User user = optionalUser.get();
        String avatarUrl = storageService.saveImage(multipartFile);
        Optional<User> updatedUser = userRepository.updateAvatar(user.getId(), avatarUrl);
        if (updatedUser.isEmpty()) throw new ItemNotFoundException(USER_NOT_FOUND_IN_DATABASE);
        return new UserResponseDto(updatedUser.get());
    }
}
