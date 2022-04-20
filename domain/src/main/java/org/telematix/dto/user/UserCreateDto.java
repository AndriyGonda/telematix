package org.telematix.dto.user;

import org.springframework.lang.NonNull;

public class UserCreateDto {

    @NonNull
    private  String username;

    @NonNull
    private  String email;

    @NonNull
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

    public UserCreateDto(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
