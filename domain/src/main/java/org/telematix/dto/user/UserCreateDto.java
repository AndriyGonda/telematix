package org.telematix.dto.user;

public class UserCreateDto {
    private  String username;
    private  String email;
    private  String password;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserCreateDto() {
    }
}
