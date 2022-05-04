package org.telematix.dto.user;

import org.telematix.models.User;

public class RegisterDto {
    private  String username;
    private  String email;
    private String password;

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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public RegisterDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User toModel() {
        User user = new User();
        user.setPasswordHash(password);
        user.setUsername(username);
        user.setEmail(email);
        user.setAdministrator(false);
        return user;
    }
}
