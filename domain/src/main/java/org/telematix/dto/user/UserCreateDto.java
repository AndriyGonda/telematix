package org.telematix.dto.user;

public class UserCreateDto {
    private  String username;
    private  String email;
    private  String password;
    private  String firstName;
    private  String lastName;
    private  String avatarUrl;

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

    public String getPassword() {
        return password;
    }

    public UserCreateDto() {
    }
}
