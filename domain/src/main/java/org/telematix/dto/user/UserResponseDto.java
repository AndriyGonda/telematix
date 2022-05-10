package org.telematix.dto.user;

import org.telematix.models.User;

public class UserResponseDto {
    private final int id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String avatarUrl;
    private final boolean administrator;

    public UserResponseDto(User instance) {
        id = instance.getId();
        username = instance.getUsername();
        email = instance.getEmail();
        firstName = instance.getFirstName();
        lastName = instance.getLastName();
        avatarUrl = instance.getAvatarUrl();
        administrator = instance.isAdministrator();
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
