package org.telematix.dto.user;

public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String avatarUrl;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public UserUpdateDto() {
    }
}
