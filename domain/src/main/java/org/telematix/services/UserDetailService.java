package org.telematix.services;

import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.telematix.models.User;
import org.telematix.repositories.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {
    public static final String USER_NOT_FOUND_EXCEPTION_PLACEHOLDER = "User with %s not found in repository.";
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_EXCEPTION_PLACEHOLDER, username));
        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority(user.isAdministrator() ? "ADMIN" : "USER"))
        );
    }
}
