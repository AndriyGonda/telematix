package org.telematix.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.telematix.models.User;
import org.telematix.repositories.ModelRepository;

@Service
public class UserService {
    private final ModelRepository<User> userRepository;

    public UserService(ModelRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }
}
