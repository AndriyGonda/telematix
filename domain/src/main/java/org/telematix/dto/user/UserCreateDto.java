package org.telematix.dto.user;

import org.telematix.models.User;

public class UserCreateDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private boolean administrator;

    public boolean isAdministrator() {
        return administrator;
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public UserCreateDto() {
    }

    public User toModel() {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
}
