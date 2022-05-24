package org.telematix.dto.user;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDto that = (UserResponseDto) o;
        return id == that.id && administrator == that.administrator && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(avatarUrl, that.avatarUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, firstName, lastName, avatarUrl, administrator);
    }
}
