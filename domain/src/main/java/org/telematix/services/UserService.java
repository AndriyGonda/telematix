package org.telematix.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.models.User;
import org.telematix.repositories.ModelRepository;
import org.telematix.repositories.RepositoryException;
import org.telematix.validators.UserValidator;
import org.telematix.validators.Validator;

@Service
public class UserService {
    public static final String USER_CREATION_FAILED = "User creation failed. Reason: %s";
    public static final String FAILED_TO_RETRIEVE_INFORMATION_ABOUT_USER = "Failed to retrieve information about user.";
    private final ModelRepository<User> userRepository;
    private final Validator<UserCreateDto> createDtoValidator = new UserValidator();

    public UserService(ModelRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.getAll()
                .stream()
                .map(UserResponseDto::new).toList();
    }

    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        createDtoValidator.validate(userCreateDto);
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setUsername(userCreateDto.getUsername());
        user.setPasswordHash(userCreateDto.getPassword());
        try {
            Optional<User> optionalUser = userRepository.saveItem(user);
            if (optionalUser.isPresent()) {
                return new UserResponseDto(optionalUser.get());
            } else {
                throw new ServiceException(FAILED_TO_RETRIEVE_INFORMATION_ABOUT_USER);
            }
        } catch (RepositoryException e) {
            throw new ServiceException(String.format(USER_CREATION_FAILED, e.getMessage()));
        }
    }
}