package org.telematix.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.telematix.dto.user.UserCreateDto;
import org.telematix.dto.user.UserResponseDto;
import org.telematix.models.User;
import org.telematix.repositories.ModelRepository;
import org.telematix.validators.UserCreateValidator;
import org.telematix.validators.Validator;

@Service
public class UserService {
    private final ModelRepository<User> userRepository;
    private final Validator<UserCreateDto> createDtoValidator = new UserCreateValidator();
    public UserService(ModelRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.getAll()
                .stream()
                .map(UserResponseDto::new).toList();
    }
//    private String hashPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }
    public void createUser(UserCreateDto userCreateDto) {
       createDtoValidator.validate(userCreateDto);
    }
//    public UserResponseDto createUser(UserCreateDto userCreateDto) {
//    }
}
